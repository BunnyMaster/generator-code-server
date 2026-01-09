package freemarker;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;

class IncludeFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("include-demo.ftl");

            template.process(null, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
