package com.auth.module.generator.config.properties;

import com.auth.module.generator.annotation.valid.FilePathValid;
import com.auth.module.generator.model.entity.generator.GeneratorConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;

@Schema(name = "GeneratorProperties", title = "生成器配置")
@Data
@ConfigurationProperties(prefix = "bunny.generator")
@Configuration
@Validated
public class GeneratorProperties {

    @Schema(title = "作者")
    private String author = "系统生成";

    @Schema(title = "当前数据库")
    @NotBlank(message = "当前数据库不能为空")
    private String database;

    @Schema(title = "忽略表前缀")
    private List<String> ignoreTablePrefixes;

    @Schema(title = "时间格式")
    private String generatorTimeFormatter = "yyyy-MM-dd HH:mm:ss";

    @FilePathValid()
    @Schema(title = "基础输出根目录")
    @NotBlank(message = "基础输出根目录不能为空")
    private String baseOutputDir;

    @Schema(title = "生成器配置")
    @NotNull(message = "生成器配置不能为空")
    private Map<String, GeneratorConfig> typeMap;

}
