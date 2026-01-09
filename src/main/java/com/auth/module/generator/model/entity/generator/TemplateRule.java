package com.auth.module.generator.model.entity.generator;

import com.auth.module.generator.annotation.valid.FilePathValid;
import com.auth.module.generator.model.enums.FilenameTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(title = "模板的生成规则")
@Data
public class TemplateRule implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "文件扩展名")
    @NotBlank(message = "文件扩展名不能为空")
    private String fileExtension;

    @Schema(title = "文件名类型")
    private FilenameTypeEnum filenameType = FilenameTypeEnum.UPPER_CAMEL;

    @FilePathValid()
    @Schema(title = "输出子目录")
    private String outputSubDir;

    @Schema(title = "是否生成")
    private Boolean isGenerate = true;

    @Schema(title = "是否覆盖已存在文件")
    private Boolean overwrite = true;

    @Schema(title = "文件前缀")
    private String prefix;

    @Schema(title = "文件名后缀")
    private String suffix;

    @Schema(title = "模板路径")
    @NotBlank(message = "模板路径不能为空")
    private String templatePath;

}