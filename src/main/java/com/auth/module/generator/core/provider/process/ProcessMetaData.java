package com.auth.module.generator.core.provider.process;

import com.auth.module.generator.model.entity.meta.MetaData;
import org.apache.commons.lang3.StringUtils;

public class ProcessMetaData {
    public static MetaData process(MetaData metaData) {
        String comment = metaData.getComment();
        metaData.setComment(StringUtils.isBlank(comment) ? "生成器默认注释" : comment);
        return metaData;
    }
}
