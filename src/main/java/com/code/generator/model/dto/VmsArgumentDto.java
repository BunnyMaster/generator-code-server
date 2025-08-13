package com.code.generator.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "VmsArgumentDto", description = "生成代码请求参数")
public class VmsArgumentDto {

    @Schema(name = "author", description = "作者名称")
    String author = "";

    @Schema(name = "packageName", description = "包名称")
    @NotBlank(message = "包名不能为空")
    String packageName;

    @Schema(name = "requestMapping", description = "requestMapping 名称")
    String requestMapping = "";

    @Schema(name = "tableNames", description = "表名列表")
    private List<String> tableNames;

    @Schema(name = "simpleDateFormat", description = "时间格式")
    private String simpleDateFormat = "yyyy-MM-dd HH:mm:ss";

    @Schema(name = "tablePrefixes", description = "去除表前缀")
    private String tablePrefixes = "";

    @Schema(name = "webBasePath", description = "前端基础生成路径")
    private String webBasePath;

    @Schema(name = "path", description = "路径")
    @NotEmpty(message = "表名称不能为空")
    private List<String> path;

    @Schema(name = "sql", description = "SQL 语句")
    private String sql;

}

