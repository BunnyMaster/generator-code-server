package com.auth.module.generator.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * 文件路径验证器
 *
 * @author bunny
 */
public class FilePathValidator implements ConstraintValidator<FilePathValid, String> {

    /**
     * 基础路径验证：允许字母、数字、中文、下划线、连字符、点、斜杠、空格
     */
    private static final Pattern BASIC_PATH_PATTERN = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5_\\-./\\\\ :]+$");

    /**
     * 最大路径长度
     */
    private int maxLength;

    @Override
    public void initialize(@NotNull FilePathValid constraintAnnotation) {
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        // 如果路径为空，验证失败
        if (path == null || path.trim().isEmpty()) {
            return false;
        }

        // 检查路径长度
        if (path.length() > maxLength) {
            return false;
        }

        // 检查基本字符格式
        return BASIC_PATH_PATTERN.matcher(path).matches();

    }
}
