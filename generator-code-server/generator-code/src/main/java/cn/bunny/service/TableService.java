package cn.bunny.service;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.DatabaseInfoMetaData;
import cn.bunny.domain.entity.TableMetaData;

import java.util.List;

public interface TableService {

    /**
     * 获取表属性
     *
     * @param tableName 表名称
     * @return 表属性
     */
    TableMetaData tableMetaData(String tableName);

    /**
     * 获取所有数据库
     *
     * @return 所有表信息
     */
    List<TableMetaData> databaseTableList(String tableName);

    /**
     * 获取列属性
     *
     * @param tableName 表名称
     * @return 当前表所有的列内容
     */
    List<ColumnMetaData> tableColumnInfo(String tableName);

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    DatabaseInfoMetaData databaseInfoMetaData();
}
