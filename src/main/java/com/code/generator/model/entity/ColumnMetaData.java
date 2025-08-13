package com.code.generator.model.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ColumnMetaData列属性数据", title = "字段列属性")
public class ColumnMetaData {

    @Schema(name = "columnName", title = "列名称")
    private String columnName;

    @Schema(name = "lowercaseName", title = "字段名称，小驼峰名称")
    private String lowercaseName;

    @Schema(name = "uppercaseName", title = "大驼峰名称")
    private String uppercaseName;

    @Schema(name = "jdbcType", title = "数据库字段类型")
    private String jdbcType;

    @Schema(name = "javaType", title = "Java类型")
    private String javaType;

    @Schema(name = "javascriptType", title = "Javascript类型")
    private String javascriptType;

    @Schema(name = "isPrimaryKey", title = "是否为主键")
    private Boolean isPrimaryKey;

    @Schema(name = "comment", title = "字段注释")
    private String comment;

}