package com.auth.module.generator.model.dto;

import com.auth.module.generator.annotation.valid.FilePathValid;
import com.auth.module.generator.model.entity.generator.GeneratorConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Schema(name = "GenerationConfigDTO", description = "生成配置参数")
@Data
public class GenerationConfigDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "表单")
    @NotNull(message = "表单不能为空")
    private transient T extra;

    @Schema(description = "作者")
    private String author = "系统生成";

    @Schema(description = "忽略表前缀")
    private List<String> ignoreTablePrefixes;

    @Schema(description = "时间格式")
    private String generatorTimeFormatter = "yyyy-MM-dd HH:mm:ss";

    @FilePathValid()
    @Schema(description = "基础输出根目录")
    @NotBlank(message = "基础输出根目录不能为空")
    private String baseOutputDir;

    @Schema(description = "类型映射")
    @Valid
    @NotNull(message = "类型映射不能为空")
    private Map<String, GeneratorConfig> typeMap;
}
