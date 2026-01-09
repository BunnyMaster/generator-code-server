package com.auth.module.generator.model.vo;

import com.auth.module.generator.model.entity.generator.TemplateRule;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "BaseGeneratorVo", title = "生成前端/后端返回参数")
@Data
@EqualsAndHashCode(callSuper = true)
public class GeneratedFileItemVO extends TemplateRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "id", title = "生成的唯一值")
    private String id;

    @Schema(name = "code", title = "生成的代码")
    private String code;

    @Schema(title = "输出文件名", description = "后面填充用")
    private String outputDirFile;

    @Schema(title = "表名", description = "后面填充用")
    private String tableName;

    @Schema(title = "格式化后的表名", description = "后面填充用")
    private String formatterTableName;

    @Schema(title = "生成类型")
    private String type;

    @Schema(title = "显示名称")
    private String displayName;
}