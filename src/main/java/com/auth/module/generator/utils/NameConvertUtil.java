package com.auth.module.generator.utils;

import lombok.experimental.UtilityClass;
import org.assertj.core.util.introspection.CaseFormatUtils;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class NameConvertUtil {

    /**
     * 转换为蛇形命名
     */
    public static String toSnakeCase(String str) {
        // 名称不能为空
        if (str == null || str.isEmpty()) {
            return str;
        }
        str = str.replace('-', '_');

        StringBuilder result = new StringBuilder();

        // 遍历字符串
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // 从字体开头开始，如果 i>0 会从字体开头前面加上下划线
            if (Character.isUpperCase(c) && i > 1) {
                result.append('_');
            }

            result.append(Character.toLowerCase(c));
        }
        return result.toString().replaceAll("_+", "_");
    }

    /**
     * 转换为烤肉串命名
     */
    public static String toKebabCase(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        // 先将下划线替换为连字符
        str = str.replace('_', '-');

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 处理大写字母
            if (Character.isUpperCase(c)) {
                // 如果不是第一个字符且前一个字符不是分隔符，则添加连字符
                if (i > 0 && str.charAt(i - 1) != '-') {
                    result.append('-');
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * 小驼峰命名
     */
    @NotNull
    public static String lowerCamelCase(String str) {
        return CaseFormatUtils.toCamelCase(str);
    }

    /**
     * 大驼峰命名
     */
    @NotNull
    public static String upperCamelCase(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        // 先转换为小驼峰，再将首字母大写
        String camelCase = CaseFormatUtils.toCamelCase(str);
        if (!camelCase.isEmpty()) {
            return Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1);
        }
        return camelCase;
    }
}
