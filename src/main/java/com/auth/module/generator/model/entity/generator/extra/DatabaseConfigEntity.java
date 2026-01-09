package com.auth.module.generator.model.entity.generator.extra;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(name = "DatabaseFormEntity", description = "数据库表单参数")
@Data
public class DatabaseConfigEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "当前数据库")
    @NotBlank(message = "当前数据库不能为空")
    private String database;

    @Schema(description = "指定生成的表名称")
    @NotEmpty(message = "指定生成的表名称不能为空")
    private List<String> tableNames;
}
