package com.auth.module.generator.core.provider.builder;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.auth.module.generator.core.provider.process.GenerationConfigProcessor;
import com.auth.module.generator.core.provider.validate.ValidationService;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import com.auth.module.generator.model.enums.FilenameTypeEnum;
import com.auth.module.generator.model.value.FlatGenerationConfig;
import com.auth.module.generator.model.value.TemplateDataModel;
import com.auth.module.generator.utils.PathBuilderUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TemplateDataModelBuilder<T> {

    private final GenerationConfigProcessor generationConfigProcessor;
    private final ValidationService validationService;

    public TemplateDataModelBuilder(GenerationConfigProcessor generationConfigProcessor, ValidationService validationService) {
        this.generationConfigProcessor = generationConfigProcessor;
        this.validationService = validationService;
    }

    /**
     * 构建模板数据模型
     *
     * @param dto            生成配置
     * @param metadata       表元数据
     * @param columnInfoList 列信息列表
     * @return 模板数据模型列表
     */
    public List<TemplateDataModel> templateDataModelList(GenerationConfigDTO<T> dto, MetaData metadata, List<ColumnMetaData> columnInfoList) {
        List<FlatGenerationConfig<T>> flatGenerationConfigs = generationConfigProcessor.flatGenerationConfigList(dto);

        return flatGenerationConfigs.stream().map(config -> {
                    List<String> ignoreTablePrefixes = config.getIgnoreTablePrefixes();
                    String timeFormatter = config.getGeneratorTimeFormatter();

                    TemplateDataModel dataModel = TemplateDataModel.builder()
                            // 基本信息
                            .ignoreTablePrefixes(ignoreTablePrefixes)
                            .generatorTimeFormatter(timeFormatter)
                            // 表信息
                            .tableName(metadata.getName())
                            .build();
                    BeanUtils.copyProperties(config, dataModel);

                    setTableMeta(dataModel, ignoreTablePrefixes);

                    // 构建输出路径
                    String buildOutputDir = PathBuilderUtil.buildOutputDir(
                            dto.getBaseOutputDir(),
                            metadata.getName(),
                            config.getTypeOutputDir(),
                            config.getOutputSubDir()
                    );

                    // 构建输出文件路径
                    String outputDirFile = PathBuilderUtil.buildOutputDirFile(
                            buildOutputDir,
                            config.getPrefix(),
                            dataModel.getFormatterTableName(),
                            config.getSuffix(),
                            config.getFileExtension(),
                            config.getFilenameType()
                    );

                    // 其他属性
                    dataModel.setCommentTime(LocalDateTimeUtil.format(LocalDateTimeUtil.now(), timeFormatter));
                    dataModel.setType(config.getDisplayName());
                    dataModel.setOutputDir(buildOutputDir);
                    dataModel.setOutputDirFile(outputDirFile);

                    // 表信息
                    dataModel.setMetaData(metadata);
                    dataModel.setColumnInfoList(columnInfoList);
                    return dataModel;
                })
                .toList();
    }

    /**
     * 设置表元数据
     *
     * @param model               模板数据模型
     * @param ignoreTablePrefixes 忽略表前缀列表
     */
    public void setTableMeta(TemplateDataModel model, List<String> ignoreTablePrefixes) {
        // 设置表名
        String tableName = model.getTableName();
        model.setTableName(tableName);

        // 设置格式化表名
        String formattedTableName = PathBuilderUtil.removeTablePrefix(tableName, ignoreTablePrefixes);
        model.setFormatterTableName(formattedTableName);

        // 短横线命名格式表名
        model.setFormatterTableNameByKebabCase(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.KEBAB_CASE));
        // 小写格式表名
        model.setFormatterTableNameByLowercaseName(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.LOWER_CASE));
        // 大写格式表名
        model.setFormatterTableNameByUppercaseName(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.UPPER_CASE));
        // 大写驼峰表名
        model.setTableNameByUppercaseName(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.UPPER_CAMEL));
        // 小写驼峰表名
        model.setTableNameByLowercaseName(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.LOWER_CAMEL));
        // 短横线分隔表名
        model.setTableKebabCase(FilenameTypeEnum.convertFilename(formattedTableName, FilenameTypeEnum.KEBAB_CASE));
    }
}
