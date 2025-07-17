package cn.bunny.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "TableMetaData", description = "表信息数据")
public class TableMetaData implements Serializable {

    @Schema(name = "tableName", description = "表名")
    private String tableName;

    @Schema(name = "handlerTableName", description = "处理后的表名称")
    private String cleanTableName;

    @Schema(name = "comment", description = "注释内容")
    private String comment;

    @Schema(name = "tableCats", description = "表目录")
    private String tableCat;

    @Schema(name = "tableType", description = "表类型（通常是\"TABLE\"）")
    private String tableType;

    @Schema(name = "className", description = "类名")
    private String className;

    @Schema(name = "columns", description = "列名称")
    private List<ColumnMetaData> columns = List.of();


}