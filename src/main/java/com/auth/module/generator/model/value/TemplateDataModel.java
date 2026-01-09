package com.auth.module.generator.model.value;

import com.auth.module.generator.model.entity.generator.TemplateRule;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(name = "TemplateDataModel", title = "模板数据模型")
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class TemplateDataModel extends TemplateRule {

    // 基本信息 GenerationConfigDTO
    @Schema(title = "作者")
    @NotBlank
    private String author;

    @Schema(title = "忽略表前缀")
    private List<String> ignoreTablePrefixes;

    @Schema(title = "时间格式")
    private String generatorTimeFormatter;

    @Schema(title = "基础输出根目录")
    private String baseOutputDir;

    // GeneratorConfig
    @Schema(title = "请求映射前缀")
    private String requestPrefix;

    @Schema(title = "基础包名称")
    private String packagePath;

    @Schema(title = "服务端输出根目录")
    private String typeOutputDir;

    @Schema(title = "显示名称")
    private String displayName;

    // TemplateRule 被继承了
    // 表信息
    @Schema(title = "指定生成的表名称")
    private String tableName;

    @Schema(name = "formatterTableName", title = "格式化表名")
    private String formatterTableName;

    @Schema(name = "formatterTableNameByKebabCase", title = "短横线命名格式表名")
    private String formatterTableNameByKebabCase;

    @Schema(name = "formatterTableNameByLowercaseName", title = "小写格式表名")
    private String formatterTableNameByLowercaseName;

    @Schema(name = "formatterTableNameByUppercaseName", title = "大写格式表名")
    private String formatterTableNameByUppercaseName;

    @Schema(name = "tableNameByUppercaseName", title = "大写驼峰表名")
    private String tableNameByUppercaseName;

    @Schema(name = "tableNameByLowercaseName", title = "小写驼峰表名")
    private String tableNameByLowercaseName;

    @Schema(name = "tableKebabCase", title = "短横线分隔表名")
    private String tableKebabCase;

    @Schema(name = "tableMetaData", title = "表元数据")
    @Valid
    private MetaData metaData;

    @Schema(name = "columnInfoList", title = "列信息列表")
    @Valid
    private List<ColumnMetaData> columnInfoList;

    // 其他属性
    @Schema(name = "commentTime", title = "注释时间格式")
    private String commentTime;

    @Schema(title = "生成类型")
    private String type;

    @Schema(title = "服务端输出根目录")
    private String outputDir;

    @Schema(name = "outputDirFile", title = "输出目录文件")
    private String outputDirFile;

}
