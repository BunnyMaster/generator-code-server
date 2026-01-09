package com.auth.module.generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "GenerationStrategyDTO", description = "生成策略")
@Data
public class GenerationStrategyDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "provider", title = "数据库名称",
            description = "生成时，根据数据库名称进行生成",
            example = "MySQL")
    @Null(message = "数据库名称不能为空")
    private String provider;

    @Schema(name = "mode", title = "生成模式",
            description = "生成时，根据模式进行生成，可以是写入、输出",
            example = "write")
    @NotNull(message = "生成模式不能为空")
    private String mode;
 
}
