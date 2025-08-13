package com.code.generator.utils;

import com.google.common.base.CaseFormat;
import org.assertj.core.util.introspection.CaseFormatUtils;

/* 类型转换，数据库转Java类型等 */
public class CamelCaseNameConvertUtil {

    /**
     * 下划线命名转驼峰命名
     *
     * @param name               原始名称，传入的值可以是
     *                           `xxx_xxx` `CaseFormat`
     *                           `caseFormat`
     * @param firstLetterCapital 首字母是否大写
     */
    public static String convertToCamelCase(String name, boolean firstLetterCapital) {
        if (name == null || name.isEmpty()) return name;

        // 转成小驼峰
        String lowerCamelCase = CaseFormatUtils.toCamelCase(name);

        // 首字母不大写
        if (!firstLetterCapital) return lowerCamelCase;

        // 将小驼峰转成大驼峰
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, lowerCamelCase);
    }
}
