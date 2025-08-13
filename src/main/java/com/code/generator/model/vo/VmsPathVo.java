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
@Schema(name = "GeneratorVo", description = "生成前端/后端路径返回参数")
public class VmsPathVo {

    @Schema(name = "id", description = "生成的唯一值")
    private String id;

    @Schema(name = "name", description = "路径名称")
    private String name;

    @Schema(name = "label", description = "显示的label")
    private String label;

    @Schema(name = "type", description = "文件夹最上级目录名称")
    private String type;

}
