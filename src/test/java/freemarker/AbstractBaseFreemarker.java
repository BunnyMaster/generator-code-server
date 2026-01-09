package freemarker;

import freemarker.template.TemplateExceptionHandler;
import org.junit.jupiter.api.BeforeEach;

import java.util.TimeZone;

public class AbstractBaseFreemarker {

    protected static freemarker.template.Configuration configuration;

    @BeforeEach
    public void before() {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_34);
        cfg.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "templates/test");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(true);
        cfg.setWrapUncheckedExceptions(true);
        // Do not fall back to higher scopes when reading a null loop variable:
        cfg.setFallbackOnNullLoopVariable(false);
        // To accomodate to how JDBC returns values; see Javadoc!
        cfg.setSQLDateAndTimeTimeZone(TimeZone.getDefault());

        configuration = cfg;
    }

}
