package cn.bunny.service.impl;

import cn.bunny.core.ZipFileService;
import cn.bunny.core.provider.IMetadataProvider;
import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.vo.GeneratorVo;
import cn.bunny.service.GeneratorService;
import cn.bunny.service.helper.GeneratorServiceImplHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 代码生成服务实现类
 * 实现基于数据库和SQL的代码生成逻辑
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final IMetadataProvider databaseMetadataProvider;
    private final IMetadataProvider sqlMetadataProvider;
    private final ZipFileService zipFileService;

    @Override
    public Map<String, List<GeneratorVo>> generateCodeByDatabase(VmsArgumentDto dto) {
        return generateCode(dto, databaseMetadataProvider);
    }

    @Override
    public Map<String, List<GeneratorVo>> generateCodeBySql(VmsArgumentDto dto) {
        return generateCode(dto, sqlMetadataProvider);
    }

    @Override
    public ResponseEntity<byte[]> downloadByZipByDatabase(VmsArgumentDto dto) {
        return downloadByZip(dto, this::generateCodeByDatabase);
    }

    @Override
    public ResponseEntity<byte[]> downloadByZipBySqL(VmsArgumentDto dto) {
        return downloadByZip(dto, this::generateCodeBySql);
    }

    /**
     * 通用代码生成方法
     *
     * @param dto      生成参数
     * @param provider 元数据提供者
     * @return 生成的代码，按表名分组
     */
    private Map<String, List<GeneratorVo>> generateCode(VmsArgumentDto dto, IMetadataProvider provider) {
        return dto.getTableNames().parallelStream()
                .flatMap(tableName -> {
                    TableMetaData tableMeta = provider.getTableMetadata(tableName);
                    List<ColumnMetaData> columns = provider.getColumnInfoList(tableName);
                    return GeneratorServiceImplHelper.getGeneratorStream(dto, tableMeta, columns);
                })
                .collect(Collectors.groupingBy(GeneratorVo::getTableName));
    }

    /**
     * 通用ZIP打包下载方法
     *
     * @param dto       生成参数
     * @param generator 代码生成函数
     * @return ZIP文件响应实体
     */
    private ResponseEntity<byte[]> downloadByZip(VmsArgumentDto dto, Function<VmsArgumentDto, Map<String, List<GeneratorVo>>> generator) {
        List<GeneratorVo> generatorVoList = generator.apply(dto)
                .values().stream()
                .flatMap(Collection::stream)
                .toList();

        byte[] zipBytes = zipFileService.createZipFile(generatorVoList);
        return GeneratorServiceImplHelper.getResponseEntity(zipBytes);
    }
}
