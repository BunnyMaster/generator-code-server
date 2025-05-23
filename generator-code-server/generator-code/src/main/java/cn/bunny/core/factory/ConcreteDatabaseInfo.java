package cn.bunny.core.factory;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.utils.TypeConvertUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;

@Component
public class ConcreteDatabaseInfo extends AbstractDatabaseInfo {

    @Value("${bunny.master.database}")
    private String currentDatabase;

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    @SneakyThrows
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
        }
    }

    /**
     * 解析 sql 表信息
     *
     * @param tableName 表名称或sql
     * @return 表西悉尼
     */
    @SneakyThrows
    @Override
    public TableMetaData getTableMetadata(String tableName) {
        TableMetaData tableMetaData;

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, tableName, new String[]{"TABLE"});

            // 获取表的注释信息
            if (tables.next()) {
                // 备注信息
                String remarks = tables.getString("REMARKS");

                // 数组名称
                String tableCat = tables.getString("TABLE_CAT");

                // 通常是"TABLE"
                String tableType = tables.getString("TABLE_TYPE");

                tableMetaData = TableMetaData.builder()
                        .tableName(tableName)
                        .comment(remarks)
                        .tableCat(tableCat)
                        .tableType(tableType)
                        .build();
            } else {
                throw new RuntimeException("数据表不存在");
            }

            return tableMetaData;
        }
    }

    /**
     * 获取[当前/所有]数据库表
     *
     * @return 所有表信息
     */
    @SneakyThrows
    public List<TableMetaData> databaseTableList(String dbName) {
        // 当前数据库数据库所有的表
        List<TableMetaData> allTableInfo = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // 当前数据库中所有的表
            ResultSet tables = metaData.getTables(dbName, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                // 表名称
                dbName = tables.getString("TABLE_NAME");

                // 设置表信息
                TableMetaData tableMetaData = getTableMetadata(dbName);

                allTableInfo.add(tableMetaData);
            }
        }

        return allTableInfo;
    }

    /**
     * 获取当前表的列属性
     *
     * @param tableName 表名称或sql
     * @return 当前表所有的列内容
     */
    @SneakyThrows
    @Override
    public List<ColumnMetaData> tableColumnInfo(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            Map<String, ColumnMetaData> map = new LinkedHashMap<>();
            // 当前表的主键
            Set<String> primaryKeyColumns = getPrimaryKeyColumns(tableName);

            // 当前表的列信息
            try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, null)) {
                while (columnsRs.next()) {
                    ColumnMetaData column = new ColumnMetaData();
                    // 列字段
                    String columnName = columnsRs.getString("COLUMN_NAME");
                    // 数据库类型
                    String typeName = columnsRs.getString("TYPE_NAME");

                    // 设置列字段
                    column.setColumnName(columnName);
                    // 列字段转成 下划线 -> 小驼峰
                    column.setLowercaseName(TypeConvertUtil.convertToCamelCase(column.getColumnName()));
                    // 列字段转成 下划线 -> 大驼峰名称
                    column.setUppercaseName(TypeConvertUtil.convertToCamelCase(column.getColumnName(), true));
                    // 字段类型
                    column.setJdbcType(typeName);
                    // 字段类型转 Java 类型
                    String javaType = TypeConvertUtil.convertToJavaType(typeName);
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
        }
    }
}
