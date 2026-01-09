package com.auth.module.generator.model.entity.generator;

import com.auth.module.generator.annotation.valid.FilePathValid;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Schema(title = "生成器的配置")
@Data
public class GeneratorConfig implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "显示名称")
    private String displayName;

    @Schema(title = "请求映射前缀")
    private String requestPrefix = "/api";

    @Schema(title = "包名称")
    @NotBlank(message = "包名称不能为空")
    private String packagePath;

    @FilePathValid()
    @Schema(title = "输出根目录")
    @NotBlank(message = "输出根目录不能为空")
    private String typeOutputDir;

    @Schema(title = "模板配置")
    @NotNull
    private Map<String, TemplateRule> templates;
}
