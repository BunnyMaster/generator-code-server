package com.auth.module.generator.annotation.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = {FilePathValidator.class})
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilePathValid {

    String message() default "文件路径格式不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 最大路径长度
     */
    int maxLength() default 255;
}