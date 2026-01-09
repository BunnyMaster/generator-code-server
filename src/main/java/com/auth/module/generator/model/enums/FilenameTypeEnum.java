package com.auth.module.generator.model.enums;

import com.auth.module.generator.utils.NameConvertUtil;
import lombok.Getter;

/**
 * 文件名命名格式枚举
 */
@Getter
public enum FilenameTypeEnum {

    /**
     * 小驼峰命名法 (lowerCamelCase)
     * 示例: userInfo, orderDetail, productService
     */
    LOWER_CAMEL("lowerCamel", "小驼峰命名法(userInfo)"),

    /**
     * 大驼峰命名法 (UpperCamelCase/PascalCase)
     * 示例: UserInfo, OrderDetail, ProductService
     */
    UPPER_CAMEL("upperCamel", "大驼峰命名法(UserInfo)"),

    /**
     * 蛇形命名法 (snake_case)
     * 示例: user_info, order_detail, product_service
     */
    SNAKE_CASE("snakeCase", "蛇形命名法(user_info)"),

    /**
     * 烤肉串命名法 (kebab-case)
     * 示例: user-info, order-detail, product-service
     */
    KEBAB_CASE("kebabCase", "烤肉串命名法(ser-info)"),

    /**
     * 全小写 (lowercase)
     * 示例: userinfo, orderdetail, productservice
     */
    LOWER_CASE("lowerCase", "全小写(userinfo)"),

    /**
     * 全大写 (UPPERCASE)
     * 示例: USERINFO, ORDERDETAIL, PRODUCTSERVICE
     */
    UPPER_CASE("upperCase", "全大写(USERINFO)"),

    /**
     * 保持原样，不进行转换
     */
    ORIGINAL("original", "保持原样");

    private final String code;
    private final String description;

    FilenameTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 转换文件名
     *
     * @param originalName 原始名称
     * @param filenameType 文件名类型
     * @return 转换后的文件名
     */
    public static String convertFilename(String originalName, FilenameTypeEnum filenameType) {
        if (originalName == null || originalName.trim().isEmpty()) {
            return originalName;
        }

        if (filenameType == null) {
            filenameType = LOWER_CAMEL;
        }

        return switch (filenameType) {
            case LOWER_CAMEL -> NameConvertUtil.lowerCamelCase(originalName);
            case UPPER_CAMEL -> NameConvertUtil.upperCamelCase(originalName);
            case SNAKE_CASE -> NameConvertUtil.toSnakeCase(originalName);
            case KEBAB_CASE -> NameConvertUtil.toKebabCase(originalName);
            case LOWER_CASE -> originalName.toLowerCase();
            case UPPER_CASE -> originalName.toUpperCase();
            default -> originalName;
        };
    }

}