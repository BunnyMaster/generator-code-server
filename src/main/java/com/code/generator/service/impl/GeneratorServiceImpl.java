package com.code.generator.service.impl;

import com.code.generator.core.provider.IMetadataProvider;
import com.code.generator.core.template.VmsTBaseTemplateGenerator;
import com.code.generator.model.dto.VmsArgumentDto;
import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.TableMetaData;
import com.code.generator.model.vo.GeneratorVo;
import com.code.generator.service.GeneratorService;
import com.code.generator.service.helper.VmsGeneratorPathHelper;
import com.code.generator.utils.ZipFileUtil;
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

    private final List<String> ignoreField = List.of("id", "update_time", "create_time", "create_user", "update_user");

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

                    // 排除不需要的字段
                    List<ColumnMetaData> columns = databaseMetadataProvider.getColumnInfoList(tableName).stream()
                            .filter(columnMetaData -> {
                                String columnName = columnMetaData.getColumnName();
                                return !ignoreField.contains(columnName);
                            })
                            .toList();
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
        // 根据Sql语句进行分析表的属性和表列字段
        String sql = dto.getSql();
        TableMetaData tableMeta = sqlMetadataProvider.getTableMetadata(sql);

        List<ColumnMetaData> columns = sqlMetadataProvider.getColumnInfoList(sql).stream()
                .filter(columnMetaData -> {
                    String columnName = columnMetaData.getColumnName();
                    return !ignoreField.contains(columnName);
                })
                .toList();

        // 生成代码
        List<GeneratorVo> generatorVoList = getGeneratorStream(dto, tableMeta, columns).toList();

        Map<String, List<GeneratorVo>> map = new HashMap<>();
        map.put(tableMeta.getTableName(), generatorVoList);

        return map;
    }

    /**
     * 根据数据库进行生成
     *
     * @param dto 生成参数
     * @return 生成的ZIP文件
     */
    @Override
    public ResponseEntity<byte[]> downloadByZipByDatabase(VmsArgumentDto dto) {
        return downloadByZip(dto, this::generateCodeByDatabase);
    }

    /**
     * 根据Sql语句及逆行生成
     *
     * @param dto 生成参数
     * @return 生成的ZIP文件
     */
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
        // 因为这里使用到了并行流，对 tableMeta 操作正常是会拿到修改后的值，但是在并行流会有线程安全问题，所以直接要手动实现深拷贝

        return dto.getPath().parallelStream().map(path -> {
            // 创建生成模板
            VmsTBaseTemplateGenerator generator = new VmsTBaseTemplateGenerator(dto, path, tableMeta);

            // 生成好的模板
            String code = generator.generateCode(tableMeta, columns).toString();
            String processVmPath = VmsGeneratorPathHelper.processVmPath(path, tableMeta.getCleanTableName(), dto);

            return GeneratorVo.builder()
                    .id(UUID.randomUUID().toString())
                    .code(code)
                    .comment(tableMeta.getComment())
                    .tableName(tableMeta.getTableName())
                    .path(processVmPath)
                    .build();
        });
    }
}
