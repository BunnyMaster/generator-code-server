package com.auth.module.generator.core.freemarker.directive;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateModel;

import java.io.IOException;
import java.util.Map;

/**
 * 重复指令
 * <@repeat str="Foo" count=3 />
 */
public class RepeatDirective implements TemplateDirectiveModel {
    @Override
    public void execute(Environment environment,
                        Map map, TemplateModel[] templateModels,
                        TemplateDirectiveBody templateDirectiveBody)
            throws IOException {

        // 当前的字符串
        String str = map.get("str").toString();

        // 需要生成几次
        int count = Integer.parseInt(map.get("count").toString());

        String result = str.repeat(count);
        environment.getOut().write(result);
    }
}
