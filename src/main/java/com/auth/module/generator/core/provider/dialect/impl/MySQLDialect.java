package com.auth.module.generator.core.provider.dialect.impl;

import com.auth.module.generator.core.provider.dialect.GeneratorCategoryDialect;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mysql 数据库方言
 */
@Component
public class MySQLDialect implements GeneratorCategoryDialect {
    private static final Pattern COMMENT_EQUALS = Pattern.compile("COMMENT\\s*=\\s*'(.*?)'", Pattern.CASE_INSENSITIVE);
    private static final Pattern COMMENT = Pattern.compile("COMMENT\\s*'(.*?)'", Pattern.CASE_INSENSITIVE);

    /**
     * 提取表注释
     *
     * @param tableOptions 选项
     * @return 注释信息
     */
    @Override
    public String extractTableComment(String tableOptions) {
        Matcher matcher = COMMENT_EQUALS.matcher(tableOptions);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 提取列注释
     *
     * @param columnSpecs 列规格
     * @return 注释信息
     */
    @Override
    public String extractColumnComment(List<String> columnSpecs) {
        String columnSpecsString = String.join(" ", columnSpecs);
        Matcher columnSpecsStringMatcher = COMMENT.matcher(columnSpecsString);
        return columnSpecsStringMatcher.find() ? columnSpecsStringMatcher.group(1) : null;
    }

    /**
     * 将数据库类型转换为Java 类型
     *
     * @param columnType 数据库中的列类型
     */
    @Override
    public String convertToJavaType(String columnType) {
        if (columnType == null) {
            return "Object";
        }

        columnType = columnType.toLowerCase();
        return switch (columnType) {
            case "varchar", "char", "text", "longtext", "mediumtext", "tinytext" -> "String";
            case "int", "integer", "tinyint", "smallint" -> "Integer";
            case "bigint" -> "Long";
            case "decimal", "numeric" -> "BigDecimal";
            case "float" -> "Float";
            case "double" -> "Double";
            case "boolean", "bit", "tinyint unsigned" -> "Boolean";
            case "date", "year" -> "Date";
            case "time" -> "Time";
            case "datetime", "timestamp" -> "LocalDateTime";
            case "blob", "longblob", "mediumblob", "tinyblob" -> "byte[]";
            default -> "Object";
        };
    }

    /**
     * 将数据库类型转换为JavaScript 类型
     *
     * @param columnType 数据库中的列类型
     */
    @Override
    public String convertToJavaScriptType(String columnType) {
        if (columnType == null || "object".equals(columnType)) {
            return "any";
        }

        columnType = StringUtils.uncapitalize(columnType);

        return switch (columnType) {
            case "double", "int", "long", "integer", "float", "decimal",
                 "short", "byte", "bigInteger", "bigDecimal" -> "number";
            case "char", "character", "string", "localDateTime",
                 "date", "localDate", "instant" -> "string";
            case "list", "set" -> "array";
            case "map" -> "record";
            case "void" -> "void";
            default -> columnType;
        };
    }

}