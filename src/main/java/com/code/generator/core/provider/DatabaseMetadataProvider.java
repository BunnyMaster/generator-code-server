package com.code.generator.core.provider;

import com.code.generator.core.dialect.DatabaseDialect;
import com.code.generator.exception.GeneratorCodeException;
import com.code.generator.exception.MetadataNotFoundException;
import com.code.generator.exception.MetadataProviderException;
import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.DatabaseInfoMetaData;
import com.code.generator.model.entity.TableMetaData;
import com.code.generator.utils.CamelCaseNameConvertUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据库元数据提供者
 * 提供从数据库获取表结构信息的实现
 */
@Component
@RequiredArgsConstructor
public class DatabaseMetadataProvider implements IMetadataProvider {

    private final HikariDataSource dataSource;
    private final DatabaseDialect dialect;

    @Value("${bunny.master.database}")
    private String currentDatabase;

    /**
     * 根据表名标识符获取单个表的元数据信息
     *
     * @param tableName 要查询的表名（大小写敏感度取决于数据库实现）
     * @return TableMetaData 包含表元数据的对象，包括：
     * - 表名
     * - 表注释/备注
     * - 表所属目录（通常是数据库名）
     * - 表类型（通常为"TABLE"）
     * @throws MetadataNotFoundException 当指定的表不存在时抛出
     * @throws GeneratorCodeException    当查询过程中发生其他异常时抛出
     */
    @Override
    public TableMetaData getTableMetadata(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});

            if (tables.next()) {
                return TableMetaData.builder()
                        .tableName(tableName)
                        .comment(tables.getString("REMARKS"))
                        .tableCat(tables.getString("TABLE_CAT"))
                        .tableType(tables.getString("TABLE_TYPE"))
                        .build();
            }
            throw new MetadataNotFoundException("Table not found: " + tableName);
        } catch (SQLException e) {
            throw new GeneratorCodeException("Failed to get metadata for table: " + tableName, e);
        }
    }


    /**
     * 获取指定表的所有列信息列表
     *
     * @param tableName 要查询的表名（大小写敏感度取决于数据库实现）
     * @return 包含所有列元数据的列表，每个列的信息封装在ColumnMetaData对象中
     * @throws MetadataProviderException 当获取列元数据过程中发生异常时抛出
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            Set<String> primaryKeys = getPrimaryKeys(tableName);
            DatabaseMetaData metaData = connection.getMetaData();

            return getColumnMetaData(metaData, tableName, primaryKeys);
        } catch (SQLException e) {
            throw new GeneratorCodeException("Failed to get column info for table: " + tableName, e);
        }
    }

    /**
     * 获取表的所有主键列名 获取表的主键列名集合
     *
     * @param tableName 表名
     * @return 主键列名的集合
     */
    private Set<String> getPrimaryKeys(String tableName) {
        Set<String> primaryKeys = new HashSet<>();
        try (Connection connection = dataSource.getConnection()) {
            ResultSet pkResultSet = connection.getMetaData().getPrimaryKeys(null, null, tableName);
            while (pkResultSet.next()) {
                primaryKeys.add(pkResultSet.getString("COLUMN_NAME").toLowerCase());
            }
        } catch (SQLException e) {
            throw new GeneratorCodeException("Get primary key error: " + e.getMessage());
        }
        return primaryKeys;
    }

    /**
     * 获取列元数据列表
     */
    private List<ColumnMetaData> getColumnMetaData(DatabaseMetaData metaData, String tableName, Set<String> primaryKeys) throws SQLException {
        Map<String, ColumnMetaData> columnMap = new LinkedHashMap<>();
        try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, null)) {
            while (columnsRs.next()) {
                ColumnMetaData column = buildColumnMetaData(columnsRs, primaryKeys);
                columnMap.putIfAbsent(column.getColumnName(), column);
            }
        }
        return columnMap.values().stream().distinct().collect(Collectors.toList());
    }

    /**
     * 构建列元数据对象
     */
    private ColumnMetaData buildColumnMetaData(ResultSet columnsRs, Set<String> primaryKeys) throws SQLException {
        String columnName = columnsRs.getString("COLUMN_NAME");
        String typeName = columnsRs.getString("TYPE_NAME");

        ColumnMetaData column = new ColumnMetaData();
        column.setColumnName(columnName);
        column.setLowercaseName(CamelCaseNameConvertUtil.convertToCamelCase(columnName, false));
        column.setUppercaseName(CamelCaseNameConvertUtil.convertToCamelCase(columnName, true));
        column.setJdbcType(typeName);
        column.setJavaType(dialect.convertToJavaType(typeName));
        column.setJavascriptType(dialect.convertToJavaScriptType(column.getJavaType()));
        column.setComment(columnsRs.getString("REMARKS"));
        column.setIsPrimaryKey(primaryKeys.contains(columnName));

        return column;
    }

    /**
     * 获取数据库的元数据信息
     *
     * @return DatabaseInfoMetaData 包含数据库基本信息的对象，包括：
     * - 数据库产品名称和版本
     * - JDBC驱动名称和版本
     * - 连接URL
     * - 用户名
     * - 当前数据库名称
     * @throws GeneratorCodeException 如果获取数据库信息时发生SQL异常
     */
    public DatabaseInfoMetaData databaseInfoMetaData() {
        // 使用try-with-resources确保Connection自动关闭
        try (Connection connection = dataSource.getConnection()) {
            // 获取数据库的元数据对象
            DatabaseMetaData metaData = connection.getMetaData();

            // 使用Builder模式构建数据库信息对象
            return DatabaseInfoMetaData.builder()
                    // 数据库产品名称(如MySQL, Oracle等)
                    .databaseProductName(metaData.getDatabaseProductName())
                    // 数据库产品版本号
                    .databaseProductVersion(metaData.getDatabaseProductVersion())
                    // JDBC驱动名称
                    .driverName(metaData.getDriverName())
                    // JDBC驱动版本
                    .driverVersion(metaData.getDriverVersion())
                    // 数据库连接URL
                    .url(metaData.getURL())
                    // 连接使用的用户名
                    .username(metaData.getUserName())
                    // 当前使用的数据库名称(由类成员变量提供)
                    .currentDatabase(currentDatabase)
                    .build();
        } catch (SQLException e) {
            // 将SQL异常转换为自定义的业务异常
            throw new GeneratorCodeException("Get database info error:" + e.getMessage());
        }
    }

    /**
     * 批量获取指定数据库中所有表的元数据信息
     * 获取指定数据库中的所有表信息
     *
     * @param dbName 数据库名称
     * @return 包含所有表元数据的列表，每个表的信息封装在TableMetaData对象中
     * @throws MetadataProviderException 如果获取元数据过程中发生异常
     */
    public List<TableMetaData> getTableMetadataBatch(String dbName) {
        // 初始化返回结果列表
        List<TableMetaData> allTableInfo = new ArrayList<>();

        // 使用try-with-resources确保Connection资源自动关闭
        try (Connection conn = dataSource.getConnection()) {
            // 获取数据库的元数据对象
            DatabaseMetaData metaData = conn.getMetaData();

            /*
             参数说明：
             1. dbName - 数据库/目录名称，null表示忽略
             2. null - 模式/模式名称，null表示忽略
             3. "%" - 表名称模式，"%"表示所有表
             4. new String[]{"TABLE"} - 类型数组，这里只查询普通表
             */
            ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});

            // 遍历查询结果集中的所有表
            while (tables.next()) {
                // 从结果集中获取表的各种属性
                String tableName = tables.getString("TABLE_NAME");  // 表名
                String remarks = tables.getString("REMARKS");      // 表备注/注释
                String tableCat = tables.getString("TABLE_CAT");   // 表所属的目录(通常是数据库名)
                String tableType = tables.getString("TABLE_TYPE");  // 表类型(这里应该是"TABLE")

                // 使用Builder模式创建TableMetaData对象并设置属性
                TableMetaData tableMetaData = TableMetaData.builder()
                        .tableName(tableName)    // 设置表名
                        .comment(remarks)       // 设置表注释
                        .tableCat(tableCat)     // 设置表所属目录
                        .tableType(tableType)   // 设置表类型
                        .build();

                // 将表元数据对象添加到结果列表
                allTableInfo.add(tableMetaData);
            }
        } catch (Exception e) {
            // 捕获任何异常并转换为自定义异常抛出
            throw new MetadataProviderException("Failed to get batch table metadata", e);
        }

        // 返回包含所有表元数据的列表
        return allTableInfo;
    }

}
