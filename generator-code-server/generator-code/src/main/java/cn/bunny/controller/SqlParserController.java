package cn.bunny.controller;

import cn.bunny.core.factory.ConcreteSqlParserDatabaseInfo;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.result.Result;
import cn.bunny.service.SqlParserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "解析SQL", description = "解析SQL接口")
@RestController
@RequestMapping("/api/sqlParser")
public class SqlParserController {

    @Resource
    private SqlParserService sqlParserService;

    @Operation(summary = "解析SQL成表信息", description = "解析SQL成表信息")
    @PostMapping("tableInfo")
    public Result<TableMetaData> tableInfo(String sql) {
        TableMetaData vo = sqlParserService.tableInfo(sql);
        return Result.success(vo);
    }

    @Operation(summary = "解析SQL成列数据", description = "解析SQL成列数据")
    @PostMapping("columnMetaData")
    public Result<List<ColumnMetaData>> columnMetaData(String sql) {
        ConcreteSqlParserDatabaseInfo databaseInfoCore = new ConcreteSqlParserDatabaseInfo();
        List<ColumnMetaData> vo = databaseInfoCore.tableColumnInfo(sql);
        return Result.success(vo);
    }
}
