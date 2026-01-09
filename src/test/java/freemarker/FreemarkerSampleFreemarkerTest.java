package freemarker;

import freemarker.template.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Map;

class FreemarkerSampleFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        HtmlAttributeDemo htmlAttributeDemo = new HtmlAttributeDemo();
        htmlAttributeDemo.setUser("我就是用户");
        htmlAttributeDemo.setLatestProduct(Map.of(
                "url", "https://freemarker.apache.org/docs/pgui_quickstart.html",
                "name", "Freemarker"
        ));

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("freemarker-sample-demo.ftl");

            template.process(htmlAttributeDemo, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class HtmlAttributeDemo {

        @Schema(description = "用户名")
        private String user;

        @Schema(description = "最新产品")
        private Map<String, String> latestProduct;

    }
}
