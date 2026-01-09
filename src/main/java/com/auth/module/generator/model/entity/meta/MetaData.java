package com.auth.module.generator.model.entity.meta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(name = "TableMetaData", description = "表信息数据")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetaData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "tableName", description = "表名")
    @NotBlank(message = "表名不能为空")
    private String name;

    @Schema(name = "comment", description = "注释内容")
    @NotBlank(message = "注释内容不能为空")
    private String comment = "NULL";

    @Schema(name = "tableCats", description = "表目录")
    private String category;

    @Schema(name = "tableType", description = "表类型（通常是\"TABLE\"）")
    private String type;

    @Schema(name = "className", description = "类名")
    private String className;

    @Schema(name = "columns", description = "列名称")
    @Valid
    private transient List<ColumnMetaData> columns;

}