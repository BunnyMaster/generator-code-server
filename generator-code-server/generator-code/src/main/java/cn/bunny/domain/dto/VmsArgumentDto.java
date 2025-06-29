package cn.bunny.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "VmsArgumentDto", description = "生成代码请求参数")
public class VmsArgumentDto {

    @Schema(name = "author", description = "作者名称")
    String author;

    @Schema(name = "packageName", description = "包名称")
    String packageName;

    @Schema(name = "requestMapping", description = "requestMapping 名称")
    String requestMapping;

    @NotNull(message = "表名称不能为空")
    @NotEmpty(message = "表名称不能为空")
    private List<String> tableNames;

    @Schema(name = "simpleDateFormat", description = "时间格式")
    private String simpleDateFormat;

    @Schema(name = "tablePrefixes", description = "去除表前缀")
    private String tablePrefixes;

    @Schema(name = "comment", description = "注释内容")
    private String comment;

    @Schema(name = "path", description = "路径")
    private List<String> path;

    @Schema(name = "sql", description = "SQL 语句")
    private String sql;
}

