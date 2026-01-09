package freemarker;

import freemarker.template.Template;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

class BuiltInFunctionFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        List<Integer> integers = List.of(1, 2, 4);
        Map<String, Object> map = Map.of(
                "testString", "this_is &",
                "testSequence", integers
        );

        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("built-in-function.ftl");

            template.process(map, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
