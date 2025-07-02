package cn.bunny.service.helper;

import cn.bunny.core.template.VmsTBaseTemplateGenerator;
import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.vo.GeneratorVo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * 代码生成服务辅助类
 * 提供生成器和响应实体的构建方法
 */
public class GeneratorServiceImplHelper {

    private static final String ZIP_FILE_PREFIX = "code-";
    private static final String ZIP_FILE_SUFFIX = ".zip";

    /**
     * 获取生成器流
     *
     * @param dto       生成参数
     * @param tableMeta 表元数据
     * @param columns   列信息
     * @return 生成器流
     */
    public static Stream<GeneratorVo> getGeneratorStream(VmsArgumentDto dto, TableMetaData tableMeta, List<ColumnMetaData> columns) {
        return dto.getPath().parallelStream().map(path -> {
            VmsTBaseTemplateGenerator generator = new VmsTBaseTemplateGenerator(dto, path, tableMeta);
            String code = generator.generateCode(tableMeta, columns).toString();

            return GeneratorVo.builder()
                    .id(UUID.randomUUID().toString())
                    .code(code)
                    .comment(tableMeta.getComment())
                    .tableName(tableMeta.getTableName())
                    .path(VmsGeneratorPathHelper.processVmPath(dto, path, tableMeta.getTableName()))
                    .build();
        });
    }

    /**
     * 构建ZIP下载响应实体
     *
     * @param zipBytes ZIP文件字节数组
     * @return 响应实体
     */
    public static ResponseEntity<byte[]> getResponseEntity(byte[] zipBytes) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + generateZipFilename());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }

    /**
     * 生成ZIP文件名
     */
    private static String generateZipFilename() {
        return ZIP_FILE_PREFIX + UUID.randomUUID().toString().split("-")[0] + ZIP_FILE_SUFFIX;
    }
}