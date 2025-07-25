package cn.bunny.service;

import cn.bunny.model.entity.DatabaseInfoMetaData;

public interface TableService {

    /**
     * 数据库所有的信息
     *
     * @return 当前连接的数据库信息属性
     */
    DatabaseInfoMetaData databaseInfoMetaData();

}
