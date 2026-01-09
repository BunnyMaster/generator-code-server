package com.auth.module.generator.service.impl;

import com.auth.module.generator.config.properties.GeneratorProperties;
import com.auth.module.generator.convert.GenerationConfigConvertMapper;
import com.auth.module.generator.core.mode.ModeStrategy;
import com.auth.module.generator.core.provider.metadata.MetadataProvider;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.DatabaseConfigEntity;
import com.auth.module.generator.model.enums.FilenameTypeEnum;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.SupportEnumsVO;
import com.auth.module.generator.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * 配置服务实现类
 *
 * @author bunny
 */
@Slf4j
@Service
public class ConfigServiceImpl implements ConfigService {
    private final GeneratorProperties generatorProperties;

    private final List<MetadataProvider> metadataProviderList;

    private final List<ModeStrategy<?>> strategyList;

    public ConfigServiceImpl(GeneratorProperties generatorProperties,
                             List<MetadataProvider> metadataProviderList,
                             List<ModeStrategy<?>> strategyList) {
        this.generatorProperties = generatorProperties;
        this.metadataProviderList = metadataProviderList;
        this.strategyList = strategyList;
    }

    /**
     * 获取数据库配置
     *
     * @return 数据库配置
     */
    @Override
    public GenerationConfigDTO<DatabaseConfigEntity> getDatabaseConfig() {
        GenerationConfigDTO<DatabaseConfigEntity> dto = GenerationConfigConvertMapper.INSTANCE.generatorPropertiesToGenerationConfigDTO(generatorProperties);

        // 设置表单信息
        DatabaseConfigEntity databaseConfigEntity = new DatabaseConfigEntity();
        databaseConfigEntity.setDatabase(generatorProperties.getDatabase());
        dto.setExtra(databaseConfigEntity);

        return dto;
    }

    /**
     * 获取 FilenameTypeEnums
     *
     * @return FilenameTypeEnums
     */
    @Override
    public List<ElementOptionVO> getFilenameTypeEnums() {
        return Arrays.stream(FilenameTypeEnum.values())
                .map(value -> ElementOptionVO.builder()
                        .label(value.getDescription())
                        .value(value.name())
                        .build())
                .toList();
    }

    /**
     * 支持哪些生成内容
     *
     * @return 支持哪些生成内容
     */
    @Override
    public SupportEnumsVO getSupportEnums() {
        // 支持自成的类型
        List<ElementOptionVO> supportProviderList = metadataProviderList.stream()
                .map(MetadataProvider::getProviderType)
                .toList();

        // 支持生成内容
        List<ElementOptionVO> supportModeList = strategyList.stream()
                .map(ModeStrategy::getModeName)
                .toList();

        return SupportEnumsVO.builder()
                // 支持自成的类型
                .supportProvider(supportProviderList)
                .supportModes(supportModeList)
                .build();
    }
}
