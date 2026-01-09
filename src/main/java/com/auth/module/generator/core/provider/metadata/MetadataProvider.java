package com.auth.module.generator.core.provider.metadata;

import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import com.auth.module.generator.model.value.TemplateDataModel;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.model.vo.GenerationResultVO;

import java.util.List;

public interface MetadataProvider {
    /**
     * 获取元数据提供器的类型
     *
     * @return supportProvider 元数据提供器的类型
     */
    ElementOptionVO getProviderType();

    /**
     * 根据表标识符获取表的元数据信息
     *
     * @param identifier 表的唯一标识符
     * @return TableMetaData 表的元数据信息对象
     */
    MetaData getMetadata(String identifier);

    /**
     * 根据表标识符获取列信息列表
     *
     * @param identifier 表的唯一标识符
     * @return List<ColumnMetaData> 列元数据信息列表
     */
    List<ColumnMetaData> getColumnInfoList(String identifier);

    /**
     * 获取元数据提供器的模板数据模型列表
     *
     * @param dto 配置信息
     * @return List<TemplateDataModel> 模板数据模型列表
     */
    List<TemplateDataModel> getTemplateDataModels(GenerationConfigDTO<Object> dto);

    /**
     * 获取元数据提供器的结果列表
     *
     * @param dto 生成器配置
     * @return List<GenerationResultVO> 结果列表
     */
    List<GenerationResultVO> getResultList(GenerationConfigDTO<Object> dto);
}
