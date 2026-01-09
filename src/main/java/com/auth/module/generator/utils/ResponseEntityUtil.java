package com.auth.module.generator.utils;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@UtilityClass
public class ResponseEntityUtil {

    @NotNull
    public static HttpHeaders getHttpHeaders(String filename, byte[] result) {
        HttpHeaders headers = new HttpHeaders();

        // 设置内容类型
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        // 使用RFC 5987编码文件名，解决中文乱码问题
        headers.set("Content-Disposition", filename);

        // 禁用缓存
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        headers.setPragma("no-cache");
        headers.setExpires(0L);

        // 设置内容长度
        headers.setContentLength(result.length);
        return headers;
    }
}
