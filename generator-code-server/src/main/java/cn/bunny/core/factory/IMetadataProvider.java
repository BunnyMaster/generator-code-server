package cn.bunny.core.factory;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;

import java.util.List;

public interface IMetadataProvider {

    /**
     * 解析 sql 表信息
     *
     * @param identifier 表名称或sql
     * @return 表西悉尼
     */
    TableMetaData getTableMetadata(String identifier);

    /**
     * 获取当前表的列属性
     *
     * @param identifier 表名称或sql
     * @return 当前表所有的列内容
     */
    List<ColumnMetaData> getColumnInfoList(String identifier);

}
