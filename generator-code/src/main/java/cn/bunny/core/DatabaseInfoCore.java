package cn.bunny.core;

import cn.bunny.dao.entity.TableMetaData;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

/* 数据库信息内容 */
@Component
public class DatabaseInfoCore {

    @Resource
    private DataSource dataSource;

    /**
     * 获取表的所有主键列名
     *
     * @param tableName 表名
     * @return 主键列名的集合
     */
    @SneakyThrows
    public Set<String> getPrimaryKeyColumns(String tableName) {
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
        }
    }

    /**
     * 获取表注释信息
     *
     * @param tableName 数据库表名
     * @return 表信息
     */
    @SneakyThrows
    public TableMetaData tableInfoMetaData(String tableName) {
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
}
