package com.auth.module.generator.convert;

import com.auth.module.generator.config.properties.GeneratorProperties;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.DatabaseConfigEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenerationConfigConvertMapper {

    GenerationConfigConvertMapper INSTANCE = Mappers.getMapper(GenerationConfigConvertMapper.class);

    /**
     * GeneratorProperties 转为 GenerationConfigDTO
     *
     * @param generatorProperties GeneratorProperties
     * @return GenerationConfigDTO
     */
    GenerationConfigDTO<DatabaseConfigEntity> generatorPropertiesToGenerationConfigDTO(GeneratorProperties generatorProperties);
}
