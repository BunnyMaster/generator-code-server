package com.auth.module.generator.core.provider.process;

import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ProcessColumnMetaData {
    public static void process(List<ColumnMetaData> columnInfoList) {
        columnInfoList.forEach(columnInfo -> {
            String comment = columnInfo.getComment();
            columnInfo.setComment(StringUtils.isBlank(comment) ? "生成器默认注释" : comment);
        });
    }
}
