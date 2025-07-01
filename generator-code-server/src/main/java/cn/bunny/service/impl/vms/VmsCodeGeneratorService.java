package cn.bunny.service.impl.vms;

import cn.bunny.core.factory.DatabaseMetadataProvider;
import cn.bunny.core.factory.SqlMetadataProvider;
import cn.bunny.core.template.VmsArgumentDtoBaseTemplateGenerator;
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

    private final DatabaseMetadataProvider databaseInfoCore;
    private final SqlMetadataProvider sqlParserDatabaseInfo;

    /**
     * 根据DTO生成代码模板
     *
     * @param dto 包含生成参数的数据传输对象
     * @return 生成的代码模板列表
     */
    public Map<String, List<GeneratorVo>> generateCode(VmsArgumentDto dto) {
        String sql = dto.getSql();

        return dto.getTableNames().stream()
                .map(tableName -> {
                    TableMetaData tableMetaData = getTableMetadata(dto, tableName);
                    List<ColumnMetaData> columnInfoList = getColumnInfoList(sql, tableName);

                    return dto.getPath().stream()
                            .map(path -> {
                                VmsArgumentDtoBaseTemplateGenerator generator = new VmsArgumentDtoBaseTemplateGenerator(dto, path, tableMetaData);
                                StringWriter writer = generator.generatorCodeTemplate(tableMetaData, columnInfoList);
                                String processedPath = VmsUtil.handleVmFilename(path, tableMetaData.getTableName());

                                return GeneratorVo.builder()
                                        .id(UUID.randomUUID().toString())
                                        .code(writer.toString())
                                        .comment(tableMetaData.getComment())
                                        .tableName(tableMetaData.getTableName())
                                        .path(processedPath)
                                        .build();
                            })
                            .toList();
                })
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(GeneratorVo::getTableName));
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
                ? sqlParserDatabaseInfo.getTableMetadata(dto.getSql())
                : databaseInfoCore.getTableMetadata(tableName);
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
                ? sqlParserDatabaseInfo.getColumnInfoList(sql)
                : databaseInfoCore.getColumnInfoList(tableName).stream().distinct().toList();
    }

}