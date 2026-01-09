package freemarker;

import freemarker.template.Template;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.math.BigDecimal;

class ConditionalJudgmentFreemarkerTest extends AbstractBaseFreemarker {

    @Test
    void mainTest() {
        // 设置基础价格
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(10));
        product.setOtherPrice(BigDecimal.valueOf(20));
        product.setFlag(true);

        // 生成模板
        try (StringWriter writer = new StringWriter()) {
            Template template = configuration.getTemplate("conditional-judgment-demo.ftl");

            template.process(product, writer);
            String string = writer.toString();
            Assertions.assertNotNull(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    public static class Product {

        @Schema(description = "价格")
        private BigDecimal price;

        @Schema(description = "其他价格")
        private BigDecimal otherPrice;

        @Schema(description = "标志")
        private Boolean flag;

    }

}
