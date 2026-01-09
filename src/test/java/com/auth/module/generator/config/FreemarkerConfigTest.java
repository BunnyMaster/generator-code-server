package com.auth.module.generator.config;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.StringWriter;

@SpringBootTest
class FreemarkerConfigTest {

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 测试 FreemarkerConfig 的共享变量
     */
    @Test
    void onApplicationEvent() {
        try (StringWriter writer = new StringWriter()) {
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            Template template = configuration.getTemplate("generator/config/test-config.ftl");
            template.process(null, writer);

            String string = writer.toString();
            System.out.println(string);
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}