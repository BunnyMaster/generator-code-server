package cn.bunny.service.impl;

import cn.bunny.core.factory.ConcreteSqlParserDatabaseInfo;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.service.SqlParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqlParserServiceImpl implements SqlParserService {

    private final ConcreteSqlParserDatabaseInfo sqlParserDatabaseInfo;

    /**
     * 解析SQL内容
     *
     * @param sql Sql语句
     * @return 表信息内容
     */
    @Override
    public TableMetaData tableInfo(String sql) {
        TableMetaData tableInfoVo = new TableMetaData();

        TableMetaData tableMetaData = sqlParserDatabaseInfo.getTableMetadata(sql);
        BeanUtils.copyProperties(tableMetaData, tableInfoVo);
        return tableInfoVo;
    }
}
