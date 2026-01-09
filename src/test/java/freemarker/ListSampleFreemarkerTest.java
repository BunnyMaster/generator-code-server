package freemarker;

import freemarker.template.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ListSampleFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        List<Animal> animals = List.of(
                new Animal("DOG", BigDecimal.valueOf(10)),
                new Animal("CAT", BigDecimal.valueOf(20)),
                new Animal("BIRD-A", BigDecimal.valueOf(30)),
                new Animal("BIRD-B", BigDecimal.valueOf(40)),
                new Animal("BIRD", BigDecimal.valueOf(50))
        );

        // 使用 Map 包装数据
        Map<String, Object> data = new HashMap<>();
        data.put("animals", animals);

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("list-sample-demo.ftl");

            template.process(data, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @AllArgsConstructor
    public static class Animal {

        @Schema(description = "名称")
        private String name;

        @Schema(description = "价格")
        private BigDecimal price;

    }

}
