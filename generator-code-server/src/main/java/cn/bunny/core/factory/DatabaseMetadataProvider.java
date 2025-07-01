package cn.bunny.core.factory;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.exception.GeneratorCodeException;
import cn.bunny.exception.MetadataNotFoundException;
import cn.bunny.exception.MetadataProviderException;
import cn.bunny.utils.MysqlTypeConvertUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DatabaseMetadataProvider implements IMetadataProvider {

    private final DataSource dataSource;

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
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    public DatabaseInfoMetaData databaseInfoMetaData() {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            return DatabaseInfoMetaData.builder()
                    .databaseProductName(metaData.getDatabaseProductName())
                    .databaseProductVersion(metaData.getDatabaseProductVersion())
                    .driverName(metaData.getDriverName())
                    .driverVersion(metaData.getDriverVersion())
                    .url(metaData.getURL())
                    .username(metaData.getUserName())
                    .currentDatabase(currentDatabase)
                    .build();
        } catch (SQLException e) {
            throw new GeneratorCodeException("Get database info error:" + e.getMessage());
        }
    }

    /**
     * 解析 sql 表信息
     *
     * @param identifier 表名称或sql
     * @return 表西悉尼
     */
    @Override
    public TableMetaData getTableMetadata(String identifier) {
        TableMetaData tableMetaData;

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, identifier, new String[]{"TABLE"});

            // 获取表的注释信息
            if (tables.next()) {
                // 备注信息
                String remarks = tables.getString("REMARKS");

                // 数组名称
                String tableCat = tables.getString("TABLE_CAT");

                // 通常是"TABLE"
                String tableType = tables.getString("TABLE_TYPE");

                tableMetaData = TableMetaData.builder()
                        .tableName(identifier)
                        .comment(remarks)
                        .tableCat(tableCat)
                        .tableType(tableType)
                        .build();
            } else {
                throw new MetadataNotFoundException("Table not found: " + identifier);
            }

            return tableMetaData;
        } catch (Exception e) {
            throw new GeneratorCodeException(e.getMessage());
        }
    }

    /**
     * 获取[当前/所有]数据库表
     *
     * @return 所有表信息
     */
    public List<TableMetaData> getTableMetadataBatch(String dbName) {
        List<TableMetaData> allTableInfo = new ArrayList<>();

        try (Connection conn = dataSource.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                String remarks = tables.getString("REMARKS");
                String tableCat = tables.getString("TABLE_CAT");
                String tableType = tables.getString("TABLE_TYPE");

                TableMetaData tableMetaData = TableMetaData.builder()
                        .tableName(tableName)
                        .comment(remarks)
                        .tableCat(tableCat)
                        .tableType(tableType)
                        .build();

                allTableInfo.add(tableMetaData);
            }
        } catch (Exception e) {
            throw new MetadataProviderException("Failed to get batch table metadata", e);
        }

        return allTableInfo;
    }

    /**
     * 获取当前表的列属性
     *
     * @param identifier 表名称或sql
     * @return 当前表所有的列内容
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String identifier) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            Map<String, ColumnMetaData> map = new LinkedHashMap<>();
            // 当前表的主键
            Set<String> primaryKeyColumns = getPrimaryKeys(identifier);

            // 当前表的列信息
            try (ResultSet columnsRs = metaData.getColumns(null, null, identifier, null)) {
                while (columnsRs.next()) {
                    ColumnMetaData column = new ColumnMetaData();
                    // 列字段
                    String columnName = columnsRs.getString("COLUMN_NAME");
                    // 数据库类型
                    String typeName = columnsRs.getString("TYPE_NAME");

                    // 设置列字段
                    column.setColumnName(columnName);
                    // 列字段转成 下划线 -> 小驼峰
                    column.setLowercaseName(MysqlTypeConvertUtil.convertToCamelCase(column.getColumnName()));
                    // 列字段转成 下划线 -> 大驼峰名称
                    column.setUppercaseName(MysqlTypeConvertUtil.convertToCamelCase(column.getColumnName(), true));
                    // 字段类型
                    column.setJdbcType(typeName);
                    // 字段类型转 Java 类型
                    String javaType = MysqlTypeConvertUtil.convertToJavaType(typeName);
                    column.setJavaType(javaType);
                    // 字段类型转 JavaScript 类型
                    column.setJavascriptType(StringUtils.uncapitalize(javaType));
                    // 备注信息
                    column.setComment(columnsRs.getString("REMARKS"));

                    // 确保 primaryKeyColumns 不为空
                    if (!primaryKeyColumns.isEmpty()) {
                        // 是否是主键
                        boolean isPrimaryKey = primaryKeyColumns.contains(columnName);
                        column.setIsPrimaryKey(isPrimaryKey);
                    }

                    map.putIfAbsent(column.getColumnName(), column);
                }
            }

            return new ArrayList<>(map.values());
        } catch (Exception e) {
            throw new MetadataProviderException("Failed to get table metadata for: " + identifier, e);
        }
    }
}
