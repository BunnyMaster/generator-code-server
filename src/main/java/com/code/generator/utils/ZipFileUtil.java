package com.code.generator.utils;


import com.code.generator.model.vo.GeneratorVo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * VMS代码生成系统的ZIP打包服务
 * <p>
 * 提供将生成的代码模板打包为ZIP文件并支持下载的功能
 */
public class ZipFileUtil {

    private static final String FILE_EXTENSION = ".vm";
    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    /**
     * 创建ZIP文件
     *
     * @param generatorVoList 生成的代码列表
     * @return ZIP文件字节数组
     * @throws RuntimeException 打包失败时抛出
     */
    public static byte[] createZipFile(List<GeneratorVo> generatorVoList) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream, StandardCharsets.UTF_8)) {

            generatorVoList.forEach(vo -> addToZip(zipOutputStream, vo));
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create ZIP file", e);
        }
    }

    /**
     * 添加文件到ZIP 将单个代码文件添加到ZIP输出流
     *
     * @param zipOutputStream ZIP文件输出流
     * @param generatorVo     代码生成结果对象，包含文件路径和内容
     * @throws RuntimeException 当文件添加失败时抛出，包含失败文件路径信息
     */
    private static void addToZip(ZipOutputStream zipOutputStream, GeneratorVo generatorVo) {
        try {
            String entryPath = generatorVo.getPath().replace(FILE_EXTENSION, "");
            zipOutputStream.putNextEntry(new ZipEntry(entryPath));
            zipOutputStream.write(generatorVo.getCode().getBytes(UTF_8));
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to ZIP: " + generatorVo.getPath(), e);
        }
    }
}