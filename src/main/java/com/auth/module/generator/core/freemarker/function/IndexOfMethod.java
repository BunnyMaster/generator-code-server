package com.auth.module.generator.core.freemarker.function;

import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.util.List;

/**
 * Freemarker方法
 * 使用方式 ${indexOf("met", x)}
 */
public class IndexOfMethod implements TemplateMethodModelEx {

    @Override
    public Object exec(List arguments) throws TemplateModelException {
        // 检查参数数量
        if (arguments.size() != 2) {
            throw new TemplateModelException("indexOf method requires exactly 2 arguments: (substring, string)");
        }

        try {
            // 正确获取字符串参数
            String substring = getString(arguments.get(0));
            String mainString = getString(arguments.get(1));

            // 执行 indexOf 操作
            return mainString.indexOf(substring);

        } catch (ClassCastException e) {
            throw new TemplateModelException("Invalid argument types for indexOf method. Expected two strings.");
        }
    }

    /**
     * 安全地从 TemplateModel 中获取字符串值
     */
    private String getString(Object templateModel) throws TemplateModelException {
        return switch (templateModel) {
            case SimpleScalar simplescalar -> simplescalar.getAsString();
            case String string -> string;
            case TemplateModel model ->
                    throw new TemplateModelException("Unsupported parameter type: " + model.getClass().getName());
            default ->
                    throw new TemplateModelException("Expected string parameter, got: " + templateModel.getClass().getName());
        };
    }

}