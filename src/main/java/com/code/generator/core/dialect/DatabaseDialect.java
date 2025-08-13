package com.code.generator.core.dialect;

import java.util.List;

/**
 * 数据库方言
 */
public interface DatabaseDialect {

    /**
     * 将数据库类型转换为Java类型
     *
     * @param columnType 数据库中的列类型
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
     * 将数据库类型转换为JavaScript类型
     *
     * @param columnType 数据库中的列类型
     */
    String convertToJavaScriptType(String columnType);

}