package com.auth.module.generator.config.properties;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Schema(description = "应用信息")
@Data
@ConfigurationProperties(prefix = "bunny.app-info")
@Configuration
public class ApplicationInformationProperties {

    @Schema(description = "应用名称")
    private String title;

    @Schema(description = "应用描述")
    private String description;

    @Schema(description = "应用摘要")
    private String summary;

    @Schema(description = "应用版本")
    private String version;

}
