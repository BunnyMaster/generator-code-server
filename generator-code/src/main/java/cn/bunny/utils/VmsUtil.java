package cn.bunny.utils;

import cn.bunny.dao.dto.VmsArgumentDto;
import cn.bunny.dao.entity.ColumnMetaData;
import cn.bunny.dao.vo.TableInfoVo;
import com.google.common.base.CaseFormat;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class VmsUtil {

    private static final Map<String, String> TYPE_MAPPINGS = Map.of(
            "controller", "Controller",
            "service", "Service",
            "serviceImpl", "ServiceImpl",
            "mapper", "Mapper",
            "resourceMapper", "Mapper"
    );

    /**
     * 生成模板
     *
     * @param dto            类名称可以自定义，格式为 xxx_xxx
     * @param path           当前路径
     * @param tableMetaData  表属性
     * @param columnInfoList 列属性数组
     * @return StringWriter
     */
    public static StringWriter buildGeneratorCodeTemplate(VmsArgumentDto dto, String path, TableInfoVo tableMetaData, List<ColumnMetaData> columnInfoList) {
        StringWriter writer = new StringWriter();
        String date = new SimpleDateFormat(dto.getSimpleDateFormat()).format(new Date());
        List<String> list = columnInfoList.stream().map(ColumnMetaData::getColumnName).toList();

        // 添加要生成的属性
        VelocityContext context = new VelocityContext();

        // 类名称如果是小驼峰，需要 [手写] 为 [下划线] 之后由 [代码 -> 小驼峰/大驼峰]
        String className = dto.getClassName();
        // 去除表开头前缀
        String tablePrefixes = dto.getTablePrefixes();

        // vm 不能直接写 `{` 需要转换下
        context.put("leftBrace", "{");
        // 当前日期
        context.put("date", date);
        // 作者名字
        context.put("author", dto.getAuthor());
        // 每个 Controller 上的请求前缀
        context.put("requestMapping", dto.getRequestMapping());
        // 当前的表名
        context.put("tableName", tableMetaData.getTableName());
        // 表字段的注释内容
        context.put("comment", dto.getComment());
        // 设置包名称
        context.put("package", dto.getPackageName());
        // 当前表的列信息
        context.put("columnInfoList", columnInfoList);
        // 数据库sql列
        context.put("baseColumnList", String.join(",", list));

        // 将 表前缀  转成数组
        String replaceTableName = "";
        for (String prefix : tablePrefixes.split("[,，]")) {
            replaceTableName = className.replace(prefix, "");
        }

        // 将类名称转成小驼峰
        String toCamelCase = ConvertUtil.convertToCamelCase(replaceTableName);
        context.put("classLowercaseName", toCamelCase);
        // 将类名称转成大驼峰
        String convertToCamelCase = ConvertUtil.convertToCamelCase(replaceTableName, true);
        context.put("classUppercaseName", convertToCamelCase);
        // Velocity 生成模板
        Template servicePathTemplate = Velocity.getTemplate("vms/" + path, "UTF-8");
        servicePathTemplate.merge(context, writer);

        return writer;
    }

    /**
     * 处理 vm 文件名
     *
     * @param path      文件路径
     * @param className 类名
     */
    public static String handleVmFilename(String path, String className) {
        String[] splitPaths = path.split("/");
        int splitPathsSize = splitPaths.length - 1;

        // 大驼峰名称
        String CamelCase = ConvertUtil.convertToCamelCase(className, true);
        // 小驼峰名称
        String camelCase = ConvertUtil.convertToCamelCase(className);

        // 当前文件名
        String filename = splitPaths[splitPathsSize];
        filename = filename.replace(".vm", "");

        String[] split = filename.split("\\.");
        // 文件名称
        String name = split[0];
        // 文件扩展名
        String extension = "";
        if (split.length >= 2) {
            extension = split[1];
        }

        // 判断是否是 Java 或者 xml 文件
        String typeMappingsFilename = TYPE_MAPPINGS.get(name);
        typeMappingsFilename = typeMappingsFilename == null ? "" : typeMappingsFilename;
        if (filename.contains("java") || filename.contains("xml")) {
            filename = CamelCase + typeMappingsFilename + "." + extension;
        }

        if (filename.contains("vue") && !filename.contains("index")) {
            filename = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, camelCase) + "-" + name + "." + extension;
        }

        splitPaths[splitPathsSize] = filename;
        return String.join("/", splitPaths);
    }
}
