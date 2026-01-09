package com.auth.module.generator.exception;

import com.auth.module.generator.model.vo.result.ResultCodeEnum;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter
@ToString
@Slf4j
public class GeneratorCodeException extends RuntimeException {
    // 描述信息
    private final String message;

    // 状态码
    private final Integer code;

    // 返回结果状态
    private final ResultCodeEnum resultCodeEnum;

    public GeneratorCodeException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
        this.resultCodeEnum = null;
    }

    public GeneratorCodeException(String message, Exception exception) {
        super(message);
        this.message = message;
        this.code = 500;
        this.resultCodeEnum = null;
        log.error(message, exception);
    }
}
