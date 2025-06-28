package cn.bunny.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
    String author = "Bunny";

    @Schema(name = "packageName", description = "包名称")
    String packageName = "cn.bunny.services";

    @Schema(name = "requestMapping", description = "requestMapping 名称")
    String requestMapping = "/api";

    @NotBlank(message = "类名称不能为空")
    @NotNull(message = "类名称不能为空")
    @Pattern(regexp = "^(?:[a-z][a-z0-9_]*|[_a-z][a-z0-9_]*)$", message = "类名称不合法")
    @Schema(name = "className", description = "类名称，格式为：xxx xxx_xxx")
    private String className;

    @NotBlank(message = "表名称不能为空")
    @NotNull(message = "表名称不能为空")
    @Pattern(regexp = "^(?:[a-z][a-z0-9_]*|[_a-z][a-z0-9_]*)$", message = "表名称不合法")
    @Schema(name = "tableName", description = "表名称")
    private String tableName;

    @Schema(name = "simpleDateFormat", description = "时间格式")
    private String simpleDateFormat = "yyyy-MM-dd HH:mm:ss";

    @Schema(name = "tablePrefixes", description = "去除表前缀")
    private String tablePrefixes = "t_,sys_,qrtz_,log_";

    @Schema(name = "comment", description = "注释内容")
    private String comment;

    @Schema(name = "path", description = "路径")
    private List<String> path;

    @Schema(name = "sql", description = "SQL 语句")
    private String sql;
}

