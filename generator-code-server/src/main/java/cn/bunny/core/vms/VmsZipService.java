package cn.bunny.core.vms;

import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.vo.GeneratorVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * VMS代码生成系统的ZIP打包服务
 * <p>
 * 提供将生成的代码模板打包为ZIP文件并支持下载的功能
 */
@Service
@RequiredArgsConstructor
public class VmsZipService {

    private final VmsCodeGeneratorService codeGeneratorService;

    /**
     * 将生成的代码模板打包为ZIP文件
     *
     * @param dto 代码生成参数DTO，包含表名、包名等配置
     * @return ZIP文件字节数组，可直接用于下载
     * @throws RuntimeException 当ZIP打包过程中发生IO异常时抛出
     */
    public byte[] createZipFile(VmsArgumentDto dto) {
        // 将二维代码生成结果扁平化为一维列表
        List<GeneratorVo> generatorVoList = codeGeneratorService.generateCode(dto)
                .values().stream()
                .flatMap(Collection::stream)
                .toList();

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            // 将所有生成的文件添加到ZIP包中
            generatorVoList.forEach(generatorVo -> addToZip(zipOutputStream, generatorVo));
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create ZIP file", e);
        }
    }

    /**
     * 将单个代码文件添加到ZIP输出流
     *
     * @param zipOutputStream ZIP文件输出流
     * @param generatorVo     代码生成结果对象，包含文件路径和内容
     * @throws RuntimeException 当文件添加失败时抛出，包含失败文件路径信息
     */
    private void addToZip(ZipOutputStream zipOutputStream, GeneratorVo generatorVo) {
        final String FILE_EXTENSION = ".vm";

        String voPath = generatorVo.getPath();

        // 标准化文件路径：移除Velocity模板扩展名
        String path = voPath.replace(FILE_EXTENSION, "");
        try {
            zipOutputStream.putNextEntry(new ZipEntry(path));

            // 以UTF-8编码写入文件内容，避免乱码问题
            zipOutputStream.write(generatorVo.getCode().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to ZIP: " + voPath, e);
        }
    }
}