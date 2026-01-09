package com.auth.module.generator.model.entity.meta;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 字段列属性
 *
 * @author bunny
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMetaData implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "columnName", title = "列名称")
    @NotBlank(message = "列名称不能为空")
    private String columnName;

    @Schema(name = "lowercaseName", title = "字段名称，小驼峰名称")
    @NotBlank(message = "字段名称不能为空")
    private String lowercaseName;

    @Schema(name = "uppercaseName", title = "大驼峰名称")
    @NotBlank(message = "字段名称不能为空")
    private String uppercaseName;

    @Schema(name = "jdbcType", title = "数据库字段类型")
    @NotBlank(message = "数据库字段类型不能为空")
    private String jdbcType;

    @Schema(name = "javaType", title = "Java 类型")
    @NotBlank(message = "Java 类型不能为空")
    private String javaType;

    @Schema(name = "javascriptType", title = "Javascript 类型")
    @NotBlank(message = "Javascript 类型不能为空")
    private String javascriptType;

    @Schema(name = "isPrimaryKey", title = "是否为主键")
    @NotNull(message = "是否为主键不能为空")
    private Boolean isPrimaryKey;

    @Schema(name = "comment", title = "字段注释")
    private String comment = "NULL";

}