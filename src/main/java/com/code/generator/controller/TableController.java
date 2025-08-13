package com.code.generator.controller;

import com.code.generator.core.provider.DatabaseMetadataProvider;
import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.DatabaseInfoMetaData;
import com.code.generator.model.entity.TableMetaData;
import com.code.generator.model.result.Result;
import com.code.generator.model.result.ResultCodeEnum;
import com.code.generator.service.TableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "数据库表控制器", description = "数据库表信息接口")
@RestController
@RequestMapping("/api/table")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;
    private final DatabaseMetadataProvider databaseMetadataProvider;

    @Operation(summary = "当前数据库信息", description = "当前连接的数据库信息")
    @GetMapping("databaseInfoMetaData")
    public Result<DatabaseInfoMetaData> databaseInfoMetaData() {
        DatabaseInfoMetaData databaseInfoMetaData = tableService.databaseInfoMetaData();
        return Result.success(databaseInfoMetaData);
    }

    @Operation(summary = "数据库所有的表", description = "获取[当前/所有]数据库表")
    @GetMapping("databaseTableList")
    public Result<List<TableMetaData>> databaseTableList(String dbName) {
        List<TableMetaData> list = databaseMetadataProvider.getTableMetadataBatch(dbName);
        return Result.success(list);
    }

    @Operation(summary = "表属性", description = "获取当前查询表属性")
    @GetMapping("tableMetaData")
    public Result<TableMetaData> tableMetaData(String tableName) {
        TableMetaData tableMetaData = databaseMetadataProvider.getTableMetadata(tableName);
        return Result.success(tableMetaData);
    }

    @Operation(summary = "表的列属性", description = "获取当前查询表中列属性")
    @GetMapping("tableColumnInfo")
    public Result<List<ColumnMetaData>> tableColumnInfo(String tableName) {
        List<ColumnMetaData> columnInfo = databaseMetadataProvider.getColumnInfoList(tableName);
        return Result.success(columnInfo, ResultCodeEnum.LOAD_FINISHED);
    }

}
