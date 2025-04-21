package cn.bunny.service.impl;

import cn.bunny.core.DatabaseInfoCore;
import cn.bunny.core.TypeConvertCore;
import cn.bunny.dao.entity.ColumnMetaData;
import cn.bunny.dao.entity.DatabaseInfoMetaData;
import cn.bunny.dao.entity.TableMetaData;
import cn.bunny.dao.vo.TableInfoVo;
import cn.bunny.service.TableService;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class TableServiceImpl implements TableService {

    @Resource
    private DataSource dataSource;

    @Resource
    private DatabaseInfoCore databaseInfoCore;

    /**
     * 获取表属性
     *
     * @param tableName 表名称
     * @return 表属性
     */
    @SneakyThrows
    @Override
    public TableInfoVo tableMetaData(String tableName) {
        TableInfoVo tableInfoVo = new TableInfoVo();

        TableMetaData tableMetaData = databaseInfoCore.tableInfoMetaData(tableName);
        BeanUtils.copyProperties(tableMetaData, tableInfoVo);

        return tableInfoVo;
    }

    /**
     * 获取[当前/所有]数据库表
     *
     * @return 所有表信息
     */
    @SneakyThrows
    @Override
    public List<TableInfoVo> databaseTableList(String dbName) {

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
                TableMetaData tableMetaData = databaseInfoCore.tableInfoMetaData(dbName);
                allTableInfo.add(tableMetaData);
            }

        }

        return allTableInfo.stream().map(tableMetaData -> {
            TableInfoVo tableInfoVo = new TableInfoVo();
            BeanUtils.copyProperties(tableMetaData, tableInfoVo);

            return tableInfoVo;
        }).toList();
    }

    /**
     * 获取当前表的列属性
     *
     * @param tableName 表名称
     * @return 当前表所有的列内容
     */
    @SneakyThrows
    @Override
    public List<ColumnMetaData> tableColumnInfo(String tableName) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            List<ColumnMetaData> columns = new ArrayList<>();

            // 当前表的主键
            Set<String> primaryKeyColumns = databaseInfoCore.getPrimaryKeyColumns(tableName);

            // 当前表的列信息
            try (ResultSet columnsRs = metaData.getColumns(null, null, tableName, null)) {
                while (columnsRs.next()) {
                    ColumnMetaData column = new ColumnMetaData();

                    // 列字段
                    String columnName = columnsRs.getString("COLUMN_NAME");

                    // 将当前表的列类型转成 Java 类型
                    String javaType = TypeConvertCore.convertToJavaType(column.getJdbcType());

                    // 设置列字段
                    column.setColumnName(columnName);

                    // 列字段转成 下划线 -> 小驼峰
                    column.setLowercaseName(TypeConvertCore.convertToCamelCase(column.getColumnName()));

                    // 列字段转成 下划线 -> 大驼峰名称
                    column.setUppercaseName(TypeConvertCore.convertToCamelCase(column.getColumnName(), true));

                    // 字段类型
                    column.setJdbcType(columnsRs.getString("TYPE_NAME"));

                    // 字段类型转 Java 类型
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
                    columns.add(column);
                }
            }

            columns.get(0).setIsPrimaryKey(true);

            return columns;
        }
    }

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    @SneakyThrows
    @Override
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
                    .build();
        }
    }
}
