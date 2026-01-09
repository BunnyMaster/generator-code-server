package freemarker;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Map;

class MathOperationFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        Map<String, BigDecimal> map = Map.of("x", BigDecimal.valueOf(666));

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("math-operation-demo.ftl");

            template.process(map, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
