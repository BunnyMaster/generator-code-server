package com.auth.module.generator.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "元素选项 DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElementOptionVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "选项标签", defaultValue = "填写标签需要自己确认，这个是显示内容")
    @NotBlank(message = "选项标签不能为空")
    private String label;

    @Schema(title = "类型")
    private String type;

    @Schema(description = "选项值", defaultValue = "填写内容需要自己确认")
    @NotBlank(message = "选项值不能为空")
    private String value;
}
