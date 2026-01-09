package com.auth.module.generator.core.provider.metadata;

import cn.hutool.core.bean.BeanUtil;
import com.auth.module.generator.core.provider.validate.ValidationService;
import com.auth.module.generator.exception.GeneratorCodeException;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.value.TemplateDataModel;
import com.auth.module.generator.model.vo.GeneratedFileItemVO;
import com.auth.module.generator.model.vo.GenerationResultVO;
import freemarker.template.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 数据库元数据提供者抽象类
 *
 * @author bunny
 */
@Component
public abstract class AbstractDatabaseMetadataProvider implements MetadataProvider {

    private final FreeMarkerConfigurer freeMarkerConfigurer;

    private final ValidationService validationService;

    protected AbstractDatabaseMetadataProvider(FreeMarkerConfigurer freeMarkerConfigurer, ValidationService validationService) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
        this.validationService = validationService;
    }

    /**
     * 生成内容
     * 将数据模型应用到指定的模板配置中，并将处理结果转换为字符串返回
     *
     * @param dataModel 模板数据模型
     * @return 处理后的模板内容字符串
     */
    protected String getTemplateGeneratorString(TemplateDataModel dataModel) {
        try {
            dataModel = validationService.validateTemplateDataModel(dataModel);
            // 获取F reeMarker 配置并处理模板
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            String templatePath = dataModel.getTemplatePath();

            // 使用 StringWriter 接收模板处理结果
            try (StringWriter writer = new StringWriter()) {
                configuration.getTemplate(templatePath).process(dataModel, writer);
                return writer.toString();
            }
        } catch (Exception e) {
            throw new GeneratorCodeException("模板处理失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取元数据提供器的结果列表
     *
     * @param dto 生成器配置
     * @return List<GenerationResultVO> 结果列表
     */
    @Override
    public List<GenerationResultVO> getResultList(GenerationConfigDTO<Object> dto) {
        List<GeneratedFileItemVO> fileItemResultList = new ArrayList<>();

        // 校验生成内容
        List<TemplateDataModel> templateDataModels = getTemplateDataModels(dto);

        for (TemplateDataModel dataModel : templateDataModels) {
            String generatorString = getTemplateGeneratorString(dataModel);

            // 构建子对象
            GeneratedFileItemVO generatedFileItemVO = BeanUtil.copyProperties(dataModel, GeneratedFileItemVO.class);
            generatedFileItemVO.setId(UUID.randomUUID().toString());
            generatedFileItemVO.setCode(generatorString);

            fileItemResultList.add(generatedFileItemVO);
        }

        return fileItemResultList.stream().collect(Collectors.groupingBy(GeneratedFileItemVO::getType))
                .values()
                .stream().map(list -> GenerationResultVO.builder()
                        .id(UUID.randomUUID().toString())
                        .displayName(list.getFirst().getDisplayName())
                        .children(list)
                        .build())
                .toList();
    }
}