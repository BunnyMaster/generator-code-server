package cn.bunny.core.vms;

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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代码生成服务，负责根据数据库表结构生成各种代码模板
 */
@Service
@RequiredArgsConstructor
public class VmsCodeGeneratorService {
    private final DatabaseMetadataProvider databaseMetadataProvider;
    private final SqlMetadataProvider sqlMetadataProvider;

    /**
     * 根据参数生成代码模板
     *
     * @param dto 包含表名、路径、SQL等生成参数
     * @return 按表名分组的代码模板列表
     */
    public Map<String, List<GeneratorVo>> generateCode(VmsArgumentDto dto) {
        return dto.getTableNames().parallelStream()  // 并行处理多表
                .flatMap(tableName -> generateTemplatesForTable(dto, tableName))
                .collect(Collectors.groupingBy(GeneratorVo::getTableName));
    }

    /**
     * 为单个表生成所有路径的模板
     */
    private Stream<GeneratorVo> generateTemplatesForTable(VmsArgumentDto dto, String tableName) {
        TableMetaData tableMeta = getTableMetadata(dto, tableName);
        List<ColumnMetaData> columns = getColumnInfoList(dto, tableName);

        return dto.getPath().stream()
                .map(path -> buildTemplateVo(dto, path, tableMeta, columns));
    }

    /**
     * 构建单个模板VO对象
     */
    private GeneratorVo buildTemplateVo(VmsArgumentDto dto, String path,
                                        TableMetaData tableMeta, List<ColumnMetaData> columns) {
        VmsTBaseTemplateGenerator generator = new VmsTBaseTemplateGenerator(dto, path, tableMeta);
        String code = generator.generateCode(tableMeta, columns).toString();
        String processedPath = VmsUtil.processVmPath(dto, path, tableMeta.getTableName());

        return GeneratorVo.builder()
                .id(UUID.randomUUID().toString())
                .code(code)
                .comment(tableMeta.getComment())
                .tableName(tableMeta.getTableName())
                .path(processedPath)
                .build();
    }

    /**
     * 获取表元数据（根据是否有SQL选择不同数据源）
     */
    private TableMetaData getTableMetadata(VmsArgumentDto dto, String tableName) {
        return StringUtils.hasText(dto.getSql())
                ? sqlMetadataProvider.getTableMetadata(dto.getSql())
                : databaseMetadataProvider.getTableMetadata(tableName);
    }

    /**
     * 获取列信息（根据是否有SQL选择不同数据源）
     */
    private List<ColumnMetaData> getColumnInfoList(VmsArgumentDto dto, String tableName) {
        return StringUtils.hasText(dto.getSql())
                ? sqlMetadataProvider.getColumnInfoList(dto.getSql())
                : databaseMetadataProvider.getColumnInfoList(tableName);
    }

}