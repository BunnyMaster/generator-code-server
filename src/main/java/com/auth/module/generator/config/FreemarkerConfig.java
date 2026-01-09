package com.auth.module.generator.config;

import com.auth.module.generator.config.properties.ApplicationInformationProperties;
import com.auth.module.generator.core.freemarker.directive.RepeatDirective;
import com.auth.module.generator.core.freemarker.function.IndexOfMethod;
import com.auth.module.generator.exception.GeneratorCodeException;
import freemarker.template.TemplateModelException;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * 覆盖默认的 FreeMarker 配置
 */
@Configuration
public class FreemarkerConfig implements ApplicationListener<ApplicationReadyEvent> {

    private final freemarker.template.Configuration configuration;

    private final ApplicationInformationProperties appInfo;

    public FreemarkerConfig(freemarker.template.Configuration configuration, ApplicationInformationProperties appInfo) {
        this.configuration = configuration;
        this.appInfo = appInfo;
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationReadyEvent event) {

        // 其他配置
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        configuration.setLogTemplateExceptions(true);
        configuration.setWrapUncheckedExceptions(true);
        configuration.setFallbackOnNullLoopVariable(false);
        configuration.setSQLDateAndTimeTimeZone(TimeZone.getDefault());
        configuration.setTemplateExceptionHandler(freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER);

        // 内置一些固定的变量
        try {
            configuration.setSharedVariable("appName", appInfo.getTitle());
            configuration.setSharedVariable("description", appInfo.getDescription());
            configuration.setSharedVariable("summary", appInfo.getSummary());
            configuration.setSharedVariable("version", appInfo.getVersion());
            configuration.setSharedVariable("date", new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis()));
            configuration.setSharedVariable("time", new SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()));
            configuration.setSharedVariable("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));

            configuration.setSharedVariable("indexOf", new IndexOfMethod());
            configuration.setSharedVariable("repeat", new RepeatDirective());
        } catch (TemplateModelException e) {
            throw new GeneratorCodeException(e.getMessage());
        }
    }
}
