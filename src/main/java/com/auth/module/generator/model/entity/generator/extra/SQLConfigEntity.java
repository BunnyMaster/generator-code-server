package com.auth.module.generator.model.entity.generator.extra;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(name = "SQLConfigEntity", description = "SQL 表单参数")
@Data
public class SQLConfigEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "SQL 语句")
    private String sql;
}
