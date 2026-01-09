package com.auth.module.generator.model.value;

import com.auth.module.generator.model.entity.generator.TemplateRule;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(title = "扁平化生成配置")
@Data
@EqualsAndHashCode(callSuper = true)
public class FlatGenerationConfig<T> extends TemplateRule implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    // 基友表单
    @Schema(title = "表单")
    private transient T extra;

    @Schema(description = "作者")
    private String author = "系统生成";

    @Schema(description = "时间格式")
    private String generatorTimeFormatter = "yyyy-MM-dd HH:mm:ss";

    @Schema(description = "忽略表前缀")
    private List<String> ignoreTablePrefixes;

    @Schema(description = "基础输出根目录")
    private String baseOutputDir;

    // 生成器的配置
    @Schema(title = "显示名称")
    private String displayName;

    @Schema(title = "请求映射前缀")
    private String requestPrefix = "/api";

    @Schema(title = "包名称")
    @NotBlank(message = "包名称不能为空")
    private String packagePath;

    @Schema(title = "输出根目录")
    private String typeOutputDir;
}
