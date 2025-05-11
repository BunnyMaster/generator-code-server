package cn.bunny.service;

import cn.bunny.domain.entity.TableMetaData;

public interface SqlParserService {
    /**
     * 解析SQL内容
     *
     * @param sql Sql语句
     * @return 表信息内容
     */
    TableMetaData tableInfo(String sql);
}
