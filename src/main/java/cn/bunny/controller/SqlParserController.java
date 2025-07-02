package cn.bunny.controller;

import cn.bunny.core.provider.SqlMetadataProvider;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "解析SQL", description = "解析SQL接口")
@RestController
@RequestMapping("/api/sqlParser")
@RequiredArgsConstructor
public class SqlParserController {

    private final SqlMetadataProvider sqlParserService;

    @Operation(summary = "解析SQL成表信息", description = "解析SQL成表信息")
    @PostMapping("tableInfo")
    public Result<TableMetaData> tableInfo(String sql) {
        TableMetaData vo = sqlParserService.getTableMetadata(sql);
        return Result.success(vo);
    }

    @Operation(summary = "解析SQL成列数据", description = "解析SQL成列数据")
    @PostMapping("columnMetaData")
    public Result<List<ColumnMetaData>> columnMetaData(String sql) {
        List<ColumnMetaData> vo = sqlParserService.getColumnInfoList(sql);
        return Result.success(vo);
    }

}
