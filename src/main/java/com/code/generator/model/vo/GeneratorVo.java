package com.code.generator.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "GeneratorVo", description = "生成前端/后端返回参数")
public class GeneratorVo {

    @Schema(name = "id", description = "生成的唯一值")
    private String id;

    @Schema(name = "code", description = "生成的代码")
    private String code;

    @Schema(name = "tableName", description = "表名")
    private String tableName;

    @Schema(name = "comment", description = "注释内容")
    private String comment;

    @Schema(name = "path", description = "生成类型路径")
    private String path;

}
