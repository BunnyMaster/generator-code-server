package com.auth.module.generator.core.provider.validate;

import com.auth.module.generator.core.provider.process.ProcessColumnMetaData;
import com.auth.module.generator.core.provider.process.ProcessMetaData;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import com.auth.module.generator.model.value.TemplateDataModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * TODO 职责划分有问题临时解决下
 */
@Service
@Validated
public class ValidationService {

    /**
     * 校验数据库类型
     *
     * @param dataModel 生成后的内容
     */
    public void validate(@Valid TemplateDataModel dataModel) {
        // 这里会自动进行校验
    }

    /**
     * 校验数据库类型
     *
     * @param dataModel 数据库类型
     */
    public TemplateDataModel validateTemplateDataModel(TemplateDataModel dataModel) {
        validate(dataModel);

        // 处理 MetaData
        MetaData metaData = dataModel.getMetaData();
        ProcessMetaData.process(metaData);

        // 处理ColumnMetaData
        List<ColumnMetaData> columnInfoList = dataModel.getColumnInfoList();
        ProcessColumnMetaData.process(columnInfoList);

        dataModel.setMetaData(metaData);
        dataModel.setColumnInfoList(columnInfoList);
        return dataModel;
    }
}