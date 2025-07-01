package cn.bunny.service.impl.vms;

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
 * 负责处理ZIP打包和下载逻辑的服务类
 */
@Service
@RequiredArgsConstructor
public class VmsZipService {

    private final VmsCodeGeneratorService codeGeneratorService;

    /**
     * 创建ZIP文件字节数组
     *
     * @param dto 生成参数
     * @return ZIP文件字节数组
     */
    public byte[] createZipFile(VmsArgumentDto dto) {
        List<GeneratorVo> generatorVoList = codeGeneratorService.generateCode(dto).values().stream().flatMap(Collection::stream).toList();
        
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            generatorVoList.forEach(generatorVo -> addToZip(zipOutputStream, generatorVo));
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create ZIP file", e);
        }
    }

    /**
     * 将单个生成结果添加到ZIP文件
     *
     * @param zipOutputStream ZIP输出流
     * @param generatorVo     生成结果对象
     */
    private void addToZip(ZipOutputStream zipOutputStream, GeneratorVo generatorVo) {
        try {
            String path = generatorVo.getPath().replace(".vm", "");
            ZipEntry zipEntry = new ZipEntry(path);
            zipOutputStream.putNextEntry(zipEntry);
            zipOutputStream.write(generatorVo.getCode().getBytes(StandardCharsets.UTF_8));
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException("Failed to add file to ZIP: " + generatorVo.getPath(), e);
        }
    }

}