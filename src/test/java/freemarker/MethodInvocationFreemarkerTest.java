package freemarker;

import freemarker.core.Environment;
import freemarker.template.*;
import freemarker.template.utility.DeepUnwrap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

class MethodInvocationFreemarkerTest extends AbstractBaseFreemarker {

    public static String repeat(String string, int count) {
        return string + "---" + count;
    }

    @Test
    void mainTest() {


        Map<String, Object> dataModel = Map.of(
                "repeat", new MyRepeatMethod(),
                "repeatDirective", new RepeatDirective(),
                "repeat2", (TemplateMethodModelEx) arguments -> {
                    if (arguments.size() != 2) {
                        throw new IllegalArgumentException("Expected 2 arguments");
                    }

                    String str = arguments.get(0).toString();

                    // 使用 DeepUnwrap 来解包 FreeMarker 包装的类型
                    Object countObj;
                    try {
                        countObj = DeepUnwrap.unwrap((TemplateModel) arguments.get(1));
                    } catch (TemplateModelException e) {
                        throw new IllegalArgumentException("Failed to unwrap number argument", e);
                    }

                    if (!(countObj instanceof Number)) {
                        throw new IllegalArgumentException("Second argument must be a number");
                    }

                    int count = ((Number) countObj).intValue();
                    return repeat(str, count);
                }
        );

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("method-invocation-demo.ftl");

            template.process(dataModel, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用类方式调用
     */
    public static class MyRepeatMethod {
        public String execute(String str, int count) {
            return str + "---" + count;
        }
    }

    /**
     * 指令方式调用
     */
    public static class RepeatDirective implements TemplateDirectiveModel {
        @Override
        public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws IOException {

            String str = params.get("str").toString();
            int count = Integer.parseInt(params.get("count").toString());

            String result = str + "---" + count;
            env.getOut().write(result);
        }
    }

}
