package com.auth.module.generator.core.provider.process;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.value.FlatGenerationConfig;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GenerationConfigProcessor {

    /**
     * 扁平化配置内容，构建模板数据模型
     *
     * @param dto 生成配置
     * @return 模板数据模型列表
     */
    public <T> List<FlatGenerationConfig<T>> flatGenerationConfigList(GenerationConfigDTO<T> dto) {
        return dto.getTypeMap().values().stream()
                .flatMap(config -> config.getTemplates().values().stream()
                        .map(templateRule -> {
                            FlatGenerationConfig<T> flatGenerationConfig = new FlatGenerationConfig<>();

                            // 基础表单
                            flatGenerationConfig.setExtra(dto.getExtra());
                            flatGenerationConfig.setAuthor(dto.getAuthor());
                            flatGenerationConfig.setGeneratorTimeFormatter(dto.getGeneratorTimeFormatter());
                            flatGenerationConfig.setIgnoreTablePrefixes(dto.getIgnoreTablePrefixes());
                            flatGenerationConfig.setBaseOutputDir(dto.getBaseOutputDir());
                            // 生成器的配置
                            flatGenerationConfig.setDisplayName(config.getDisplayName());
                            flatGenerationConfig.setRequestPrefix(config.getRequestPrefix());
                            flatGenerationConfig.setPackagePath(config.getPackagePath());
                            flatGenerationConfig.setTypeOutputDir(config.getTypeOutputDir());
                            // 模板的生成规则
                            flatGenerationConfig.setFilenameType(templateRule.getFilenameType());
                            flatGenerationConfig.setIsGenerate(templateRule.getIsGenerate());
                            flatGenerationConfig.setOverwrite(templateRule.getOverwrite());
                            flatGenerationConfig.setFileExtension(templateRule.getFileExtension());
                            flatGenerationConfig.setOutputSubDir(templateRule.getOutputSubDir());
                            flatGenerationConfig.setPrefix(templateRule.getPrefix());
                            flatGenerationConfig.setSuffix(templateRule.getSuffix());
                            flatGenerationConfig.setTemplatePath(templateRule.getTemplatePath());

                            return flatGenerationConfig;
                        })
                )
                .collect(Collectors.toList());
    }
}