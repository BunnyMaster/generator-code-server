package com.auth.module.generator.core.mode.impl;

import cn.hutool.crypto.digest.MD5;
import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.exception.GeneratorCodeException;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GeneratedFileItemVO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import com.auth.module.generator.utils.ResponseEntityUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 下载Zip 文件
 *
 * @author bunny
 */
@Component
public class DownloadZipModeStrategy implements ModeStrategy<ResponseEntity<byte[]>> {

    /**
     * 获取模式名称
     *
     * @return 模式名称
     */
    @Override
    public ElementOptionVO getModeName() {
        return ElementOptionVO.builder().value("download_zip").label("下载Zip 文件").build();
    }

    /**
     * 下载Zip 文件
     *
     * @param resultList 配置
     * @return zip 文件
     */
    @Override
    public ResponseEntity<byte[]> operation(List<GenerationResultVO> resultList) {
        byte[] bytes = getBytes(resultList);

        // 生成文件名
        String time = new SimpleDateFormat("MMddHHmmss").format(new Date());
        String filename = time + "-" + MD5.create().digestHex16(bytes) + ".zip";

        // 构建响应头
        HttpHeaders headers = ResponseEntityUtil.getHttpHeaders(filename, bytes);

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

    /**
     * 获取字节数组
     *
     * @param resultList 配置
     * @return 字节数组
     */
    @NotNull
    private byte[] getBytes(List<GenerationResultVO> resultList) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8)) {

            for (GenerationResultVO resultVO : resultList) {
                for (GeneratedFileItemVO itemVO : resultVO.getChildren()) {
                    addToZip(zipOutputStream, itemVO.getOutputDirFile(), itemVO.getCode());
                }
            }

            // 显式关闭 ZipOutputStream
            zipOutputStream.finish();
            zipOutputStream.flush();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new GeneratorCodeException("Failed to create ZIP file", e);
        }
    }

    /**
     * 将指定内容添加到 ZIP输出流中作为新的条目
     *
     * @param zipOutputStream ZIP 输出流，用于写入ZIP文件内容
     * @param entryPath       ZIP 条目的路径和名称
     * @param content         要写入ZIP 条目的文件内容
     */
    public void addToZip(@NotNull ZipOutputStream zipOutputStream, String entryPath, @NotNull String content) {
        try {
            // 创建新的ZIP 条目并写入内容
            zipOutputStream.putNextEntry(new ZipEntry(entryPath));
            zipOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new GeneratorCodeException("Failed to add file to ZIP", e);
        }
    }
}
