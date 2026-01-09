package com.auth.module.generator.core.provider.dialect;

import java.util.List;

/**
 * 生成类型方言
 * 如果使用自定义生成类型，请实现此接口
 */
public interface GeneratorCategoryDialect {

    /**
     * 将生成类型类型转换为 Java 类型
     *
     * @param columnType 生成类型中的列类型
     * @return Java 类型
     */
    String convertToJavaType(String columnType);

    /**
     * 提取表注释
     *
     * @param tableOptions 选项
     * @return 注释信息
     */
    String extractTableComment(String tableOptions);

    /**
     * 提取列注释
     *
     * @param columnSpecs 列规格
     * @return 注释信息
     */
    String extractColumnComment(List<String> columnSpecs);

    /**
     * 将生成类型类型转换为 JavaScript 类型
     *
     * @param columnType 生成类型中的列类型
     * @return JavaScript 类型
     */
    String convertToJavaScriptType(String columnType);
}