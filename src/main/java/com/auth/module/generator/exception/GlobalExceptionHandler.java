package com.auth.module.generator.exception;

import com.auth.module.generator.model.vo.result.Result;
import com.auth.module.generator.model.vo.result.ResultCodeEnum;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 全局异常拦截器
 *
 * @author bunny
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String DUPLICATE_ENTRY = "Duplicate entry";

    /**
     * 自定义异常信息
     */
    @ResponseBody
    @ExceptionHandler(GeneratorCodeException.class)
    public Result<Object> exceptionHandler(GeneratorCodeException exception) {
        Integer code = exception.getCode() != null ? exception.getCode() : 500;
        return Result.error(null, code, exception.getMessage());
    }

    /**
     * 运行时异常信息
     */
    @ResponseBody
    @ExceptionHandler(RuntimeException.class)
    public Result<Object> exceptionHandler(RuntimeException exception) {
        String message = exception.getMessage();
        message = StringUtils.hasText(message) ? message : "服务器异常";

        log.error("GlobalExceptionHandler===>运行时异常信息：{}", message, exception);

        // 解析异常
        String jsonParseError = "JSON parse error (.*)";
        Matcher jsonParseErrorMatcher = Pattern.compile(jsonParseError).matcher(message);
        if (jsonParseErrorMatcher.find()) {
            return Result.error(null, 500, "JSON 解析异常 " + jsonParseErrorMatcher.group(1));
        }

        // 数据过大
        String dataTooLongError = "Data too long for column (.*?) at row 1";
        Matcher dataTooLongErrorMatcher = Pattern.compile(dataTooLongError).matcher(message);
        if (dataTooLongErrorMatcher.find()) {
            return Result.error(null, 500, dataTooLongErrorMatcher.group(1) + " 字段数据过大");
        }

        // 主键冲突
        String primaryKeyError = "Duplicate entry '(.*?)' for key .*";
        Matcher primaryKeyErrorMatcher = Pattern.compile(primaryKeyError).matcher(message);
        if (primaryKeyErrorMatcher.find()) {
            return Result.error(null, 500, "[" + primaryKeyErrorMatcher.group(1) + "]已存在");
        }

        // corn 表达式错误
        String cronExpression = "CronExpression '(.*?)' is invalid";
        Matcher cronExpressionMatcher = Pattern.compile(cronExpression).matcher(message);
        if (cronExpressionMatcher.find()) {
            return Result.error(null, 500, "表达式 " + cronExpressionMatcher.group(1) + " 不合法");
        }

        log.error("GlobalExceptionHandler===>运行时异常信息：{}", message);
        return Result.error(null, 500, message);
    }

    /**
     * 表单验证字段
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining(", "));
        return Result.error(null, 400, errorMessage);
    }

    /**
     * 特定异常处理
     */
    @ResponseBody
    @ExceptionHandler(ArithmeticException.class)
    public Result<Object> error(ArithmeticException exception) {
        log.error("GlobalExceptionHandler===>特定异常信息：{}", exception.getMessage());

        return Result.error(null, 500, exception.getMessage());
    }

    /**
     * 处理Servlet 异常
     */
    @ResponseBody
    @ExceptionHandler(ServletException.class)
    public Result<Object> error(ServletException exception) {
        String message = exception.getMessage();
        log.error("GlobalExceptionHandler===>ServletException信息：{}", message);

        // 请求路径不存在
        String noStaticResources = "No static resource (.+)";
        Matcher noStaticResourcesMatcher = Pattern.compile(noStaticResources).matcher(message);
        if (noStaticResourcesMatcher.find()) {
            return Result.error(null, 500, "请求路径不存在");
        }

        return Result.error(null, 500, message);
    }

    /**
     * 处理SQL 异常
     */
    @ResponseBody
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException exception) {
        log.error("GlobalExceptionHandler===>处理SQL异常:{}", exception.getMessage());

        String message = exception.getMessage();
        if (message.contains(DUPLICATE_ENTRY)) {
            // 错误信息
            return Result.error(ResultCodeEnum.USER_IS_EMPTY);
        } else {
            return Result.error(ResultCodeEnum.UNKNOWN_EXCEPTION);
        }
    }
}
