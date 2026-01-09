package freemarker;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.Map;

class StringOperationFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        Map<String, String> map = Map.of("user", "字符串");

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("string-operation-demo.ftl");

            template.process(map, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
