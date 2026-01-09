package com.auth.module.generator.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(name = "GeneratorVo", description = "生成返回参数")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerationResultVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "生成的唯一值")
    private String id;

    @Schema(title = "显示名称")
    private String displayName;

    @Schema(description = "子文件")
    private List<GeneratedFileItemVO> children;

}
