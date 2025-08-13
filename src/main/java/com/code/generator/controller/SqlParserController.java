package com.code.generator.controller;

import com.code.generator.core.provider.SqlMetadataProvider;
import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.TableMetaData;
import com.code.generator.model.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SQL解析控制器
 * 提供SQL语句解析功能，提取表和列元数据
 */
@Tag(name = "解析SQL", description = "解析SQL接口")
@RestController
@RequestMapping("/api/sqlParser")
@RequiredArgsConstructor
public class SqlParserController {

    private final SqlMetadataProvider sqlMetadataProvider;

    /**
     * 解析SQL获取表信息
     *
     * @param sql SQL语句
     * @return 表元数据
     */
    @Operation(summary = "解析SQL表信息", description = "解析SQL语句提取表结构信息")
    @PostMapping("tableInfo")
    public Result<TableMetaData> tableInfo(String sql) {
        return Result.success(sqlMetadataProvider.getTableMetadata(sql));
    }

    /**
     * 解析SQL获取列信息
     *
     * @param sql SQL语句
     * @return 列元数据列表
     */
    @Operation(summary = "解析SQL列数据", description = "解析SQL语句提取列结构信息")
    @PostMapping("columnMetaData")
    public Result<List<ColumnMetaData>> columnMetaData(String sql) {
        return Result.success(sqlMetadataProvider.getColumnInfoList(sql));
    }
}