package cn.bunny.core.factory;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public abstract class AbstractDatabaseInfo {

    public DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取表的所有主键列名
     *
     * @param tableName 表名
     * @return 主键列名的集合
     */
    @SneakyThrows
    public Set<String> findPrimaryKeyColumns(String tableName) {
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
     * 解析 sql 表信息
     *
     * @param name 表名称或sql
     * @return 表西悉尼
     */
    public abstract TableMetaData getTableMetadata(String name);

    /**
     * 获取当前表的列属性
     *
     * @param name 表名称或sql
     * @return 当前表所有的列内容
     */
    public abstract List<ColumnMetaData> tableColumnInfo(String name);

}
