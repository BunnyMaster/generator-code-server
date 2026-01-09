package com.auth.module.generator.config;

import com.auth.module.generator.config.properties.ApplicationInformationProperties;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class Knife4jConfig {

    private final ApplicationInformationProperties appInfo;

    @Value("${server.port}")
    private String port;

    public Knife4jConfig(ApplicationInformationProperties appInfo) {
        this.appInfo = appInfo;
    }

    @Bean
    public OpenAPI openApi() {
        String url = "http://localhost:" + port;

        // 作者等信息
        Contact contact = new Contact().name("Bunny").email("1319900154@qq.com").url(url);
        // 使用协议
        License license = new License().name("MIT").url("https://mit-license.org");
        // 相关信息
        Info info = new Info().title(appInfo.getTitle())
                .contact(contact).license(license)
                .description(appInfo.getDescription())
                .summary(appInfo.getSummary())
                .termsOfService(url)
                .version(appInfo.getVersion());

        return new OpenAPI().info(info).externalDocs(new ExternalDocumentation());
    }

    @Bean
    public GroupedOpenApi all() {
        return GroupedOpenApi.builder().group("全部请求接口").pathsToMatch("/api/**").build();
    }
}
