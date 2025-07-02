package cn.bunny.service.impl;

import cn.bunny.core.provider.IMetadataProvider;
import cn.bunny.core.template.VmsTBaseTemplateGenerator;
import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.vo.GeneratorVo;
import cn.bunny.service.GeneratorService;
import cn.bunny.service.helper.VmsGeneratorPathHelper;
import cn.bunny.utils.ZipFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代码生成服务实现类
 * 实现基于数据库和SQL的代码生成逻辑
 */
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

    private final IMetadataProvider databaseMetadataProvider;
    private final IMetadataProvider sqlMetadataProvider;

    /**
     * 代码生成方法---数据库生成
     *
     * @param dto 生成参数
     * @return 生成的代码，按表名分组
     */
    @Override
    public Map<String, List<GeneratorVo>> generateCodeByDatabase(VmsArgumentDto dto) {
        return dto.getTableNames().parallelStream()
                .flatMap(tableName -> {
                    TableMetaData tableMeta = databaseMetadataProvider.getTableMetadata(tableName);
                    List<ColumnMetaData> columns = databaseMetadataProvider.getColumnInfoList(tableName);
                    return getGeneratorStream(dto, tableMeta, columns);
                })
                .collect(Collectors.groupingBy(GeneratorVo::getTableName));
    }

    /**
     * 代码生成方法---Sql语句生成
     *
     * @param dto 生成参数
     * @return 生成的代码，按表名分组
     */
    @Override
    public Map<String, List<GeneratorVo>> generateCodeBySql(VmsArgumentDto dto) {
        String sql = dto.getSql();
        TableMetaData tableMeta = sqlMetadataProvider.getTableMetadata(sql);
        List<ColumnMetaData> columns = sqlMetadataProvider.getColumnInfoList(sql);

        List<GeneratorVo> generatorVoList = getGeneratorStream(dto, tableMeta, columns).toList();

        Map<String, List<GeneratorVo>> map = new HashMap<>();
        map.put(tableMeta.getTableName(), generatorVoList);

        return map;
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
     * 通用ZIP打包下载方法
     *
     * @param dto       生成参数
     * @param generator 代码生成函数
     * @return ZIP文件响应实体
     */
    private ResponseEntity<byte[]> downloadByZip(VmsArgumentDto dto,
                                                 Function<VmsArgumentDto, Map<String, List<GeneratorVo>>> generator) {
        // 调用生成函数
        List<GeneratorVo> generatorVoList = generator.apply(dto)
                .values().stream()
                .flatMap(Collection::stream)
                .toList();

        // 创建Zip文件
        byte[] zipBytes = ZipFileUtil.createZipFile(generatorVoList);

        // 设置返回给前端的文件名
        String zipFilename = "code-" + UUID.randomUUID().toString().split("-")[0] + ".zip";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + zipFilename);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return new ResponseEntity<>(zipBytes, headers, HttpStatus.OK);
    }

    /**
     * 获取生成器流
     *
     * @param dto       生成参数
     * @param tableMeta 表元数据
     * @param columns   列信息
     * @return 生成器流
     */
    public Stream<GeneratorVo> getGeneratorStream(VmsArgumentDto dto, TableMetaData tableMeta, List<ColumnMetaData> columns) {
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
}
