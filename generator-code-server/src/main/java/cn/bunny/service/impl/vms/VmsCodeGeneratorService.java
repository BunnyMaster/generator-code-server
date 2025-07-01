package cn.bunny.service.impl.vms;

import cn.bunny.core.provider.DatabaseMetadataProvider;
import cn.bunny.core.provider.SqlMetadataProvider;
import cn.bunny.core.template.VmsTBaseTemplateGenerator;
import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.domain.vo.GeneratorVo;
import cn.bunny.utils.VmsUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 负责处理代码生成逻辑的服务类
 */
@Service
@RequiredArgsConstructor
public class VmsCodeGeneratorService {

    private final DatabaseMetadataProvider databaseMetadataProvider;
    private final SqlMetadataProvider sqlMetadataProvider;

    /**
     * 根据DTO生成代码模板
     *
     * @param dto 包含生成参数的数据传输对象
     * @return 按表名分组的生成的代码模板列表
     */
    public Map<String, List<GeneratorVo>> generateCode(VmsArgumentDto dto) {
        // 提前获取可复用的数据
        final String sql = dto.getSql();
        final List<String> tableNames = dto.getTableNames();
        final List<String> paths = dto.getPath();

        return tableNames.parallelStream()  // 使用并行流提高多表处理效率
                .map(tableName -> {
                    // 获取表元数据和列信息
                    TableMetaData tableMetaData = getTableMetadata(dto, tableName);
                    List<ColumnMetaData> columnInfoList = getColumnInfoList(sql, tableName);

                    // 为每个路径生成模板
                    return paths.stream()
                            .map(path -> generateTemplate(dto, path, tableMetaData, columnInfoList))
                            .toList();
                })
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(GeneratorVo::getTableName));
    }

    /**
     * 生成单个模板
     */
    private GeneratorVo generateTemplate(VmsArgumentDto dto, String path,
                                         TableMetaData tableMetaData, List<ColumnMetaData> columnInfoList) {
        VmsTBaseTemplateGenerator generator = new VmsTBaseTemplateGenerator(dto, path, tableMetaData);
        StringWriter writer = generator.generatorCodeTemplate(tableMetaData, columnInfoList);
        String processedPath = VmsUtil.handleVmFilename(path, tableMetaData.getTableName());

        return GeneratorVo.builder()
                .id(UUID.randomUUID().toString())
                .code(writer.toString())
                .comment(tableMetaData.getComment())
                .tableName(tableMetaData.getTableName())
                .path(processedPath)
                .build();
    }

    /**
     * 获取表元数据
     *
     * @param dto       生成参数
     * @param tableName 表名
     * @return 表元数据对象
     */
    private TableMetaData getTableMetadata(VmsArgumentDto dto, String tableName) {
        return StringUtils.hasText(dto.getSql())
                ? sqlMetadataProvider.getTableMetadata(dto.getSql())
                : databaseMetadataProvider.getTableMetadata(tableName);
    }

    /**
     * 获取列信息列表
     *
     * @param sql       SQL语句
     * @param tableName 表名
     * @return 列元数据列表
     */
    private List<ColumnMetaData> getColumnInfoList(String sql, String tableName) {
        return StringUtils.hasText(sql)
                ? sqlMetadataProvider.getColumnInfoList(sql)
                : databaseMetadataProvider.getColumnInfoList(tableName).stream().distinct().toList();
    }

}