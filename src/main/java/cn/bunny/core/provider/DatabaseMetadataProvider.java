package cn.bunny.core.provider;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.exception.GeneratorCodeException;
import cn.bunny.exception.MetadataNotFoundException;
import cn.bunny.exception.MetadataProviderException;
import cn.bunny.utils.MysqlTypeConvertUtil;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DatabaseMetadataProvider implements IMetadataProvider {

    private final HikariDataSource dataSource;

    @Value("${bunny.master.database}")
    private String currentDatabase;

    /**
     * 获取表的所有主键列名
     *
     * @param tableName 表名
     * @return 主键列名的集合
     */
    public Set<String> getPrimaryKeys(String tableName) {
        // 主键的key
        Set<String> primaryKeys = new HashSet<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // 当前表的主键
            ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, tableName);

            while (pkResultSet.next()) {
                // 列字段
                String columnName = pkResultSet.getString("COLUMN_NAME").toLowerCase();
                primaryKeys.add(columnName);
            }

            return primaryKeys;
        } catch (SQLException e) {
            throw new GeneratorCodeException("Get primary key error:" + e.getMessage());
        }
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
     * 根据表名标识符获取单个表的元数据信息
     *
     * @param identifier 要查询的表名（大小写敏感度取决于数据库实现）
     * @return TableMetaData 包含表元数据的对象，包括：
     * - 表名
     * - 表注释/备注
     * - 表所属目录（通常是数据库名）
     * - 表类型（通常为"TABLE"）
     * @throws MetadataNotFoundException 当指定的表不存在时抛出
     * @throws GeneratorCodeException    当查询过程中发生其他异常时抛出
     */
    public TableMetaData getTableMetadata(String identifier) {
        // 声明返回对象
        TableMetaData tableMetaData;

        // 使用try-with-resources自动管理数据库连接
        try (Connection connection = dataSource.getConnection()) {
            // 获取数据库元数据对象
            DatabaseMetaData metaData = connection.getMetaData();

            /*
              查询指定表名的元数据信息
              参数说明：
              1. null - 不限制数据库目录（catalog）
              2. null - 不限制模式（schema）
              3. identifier - 要查询的精确表名
              4. new String[]{"TABLE"} - 只查询基本表类型（排除视图等）
             */
            ResultSet tables = metaData.getTables(null, null, identifier, new String[]{"TABLE"});

            // 检查是否找到匹配的表
            if (tables.next()) {
                // 获取表的注释/备注（可能为null）
                String remarks = tables.getString("REMARKS");

                // 获取表所属的目录（通常对应数据库名）
                String tableCat = tables.getString("TABLE_CAT");

                // 获取表类型（正常表应为"TABLE"）
                String tableType = tables.getString("TABLE_TYPE");

                // 使用Builder模式构建表元数据对象
                tableMetaData = TableMetaData.builder()
                        .tableName(identifier)      // 使用传入的表名标识符
                        .comment(remarks)           // 设置表注释
                        .tableCat(tableCat)         // 设置表所属目录
                        .tableType(tableType)       // 设置表类型
                        .build();
            } else {
                // 如果结果集为空，抛出表不存在异常
                throw new MetadataNotFoundException("Table not found: " + identifier);
            }

            return tableMetaData;
        } catch (Exception e) {
            // 捕获所有异常并转换为业务异常
            throw new GeneratorCodeException("Failed to get metadata for table: " + identifier + ". Error: " + e.getMessage(), e);
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

    /**
     * 获取指定表的所有列信息列表
     *
     * @param identifier 要查询的表名（大小写敏感度取决于数据库实现）
     * @return 包含所有列元数据的列表，每个列的信息封装在ColumnMetaData对象中
     * @throws MetadataProviderException 当获取列元数据过程中发生异常时抛出
     */
    public List<ColumnMetaData> getColumnInfoList(String identifier) {
        // 使用try-with-resources确保Connection自动关闭
        try (Connection connection = dataSource.getConnection()) {
            // 获取数据库元数据对象
            DatabaseMetaData metaData = connection.getMetaData();

            // 使用LinkedHashMap保持列的顺序与数据库一致
            Map<String, ColumnMetaData> map = new LinkedHashMap<>();

            // 获取当前表的所有主键列名集合
            Set<String> primaryKeyColumns = getPrimaryKeys(identifier);

            /*
              获取指定表的所有列信息
              参数说明：
              1. null - 不限制数据库目录（catalog）
              2. null - 不限制模式（schema）
              3. identifier - 要查询的表名
              4. null - 不限制列名模式（获取所有列）
             */
            try (ResultSet columnsRs = metaData.getColumns(null, null, identifier, null)) {
                // 遍历结果集中的所有列
                while (columnsRs.next()) {
                    ColumnMetaData column = new ColumnMetaData();

                    // 获取列的基本信息
                    String columnName = columnsRs.getString("COLUMN_NAME");  // 列名（原始字段名）
                    String typeName = columnsRs.getString("TYPE_NAME");      // 数据库类型名称

                    // 设置列基本信息
                    column.setColumnName(columnName);  // 设置原始列名

                    // 列名格式转换：下划线命名 -> 小驼峰命名（首字母小写）
                    String lowercaseName = MysqlTypeConvertUtil.convertToCamelCase(column.getColumnName(), false);
                    column.setLowercaseName(lowercaseName);

                    // 列名格式转换：下划线命名 -> 大驼峰命名（首字母大写）
                    String uppercaseName = MysqlTypeConvertUtil.convertToCamelCase(column.getColumnName(), true);
                    column.setUppercaseName(uppercaseName);

                    // 设置数据库类型
                    column.setJdbcType(typeName);

                    // 数据库类型 -> Java类型转换
                    String javaType = MysqlTypeConvertUtil.convertToJavaType(typeName);
                    column.setJavaType(javaType);

                    // Java类型 -> JavaScript类型转换（首字母小写）
                    column.setJavascriptType(StringUtils.uncapitalize(javaType));

                    // 设置列注释（可能为null）
                    column.setComment(columnsRs.getString("REMARKS"));

                    // 如果主键集合不为空，检查当前列是否是主键
                    if (!primaryKeyColumns.isEmpty()) {
                        boolean isPrimaryKey = primaryKeyColumns.contains(columnName);
                        column.setIsPrimaryKey(isPrimaryKey);
                    }

                    // 将列信息存入Map，避免重复（使用putIfAbsent保证不覆盖已有值）
                    map.putIfAbsent(column.getColumnName(), column);
                }
            }

            // 将Map中的值转换为List返回
            return map.values().stream().distinct().toList();
        } catch (Exception e) {
            // 捕获所有异常并转换为自定义异常，包含表名和原始异常信息
            throw new MetadataProviderException("Failed to get table metadata for: " + identifier, e);
        }
    }
}
