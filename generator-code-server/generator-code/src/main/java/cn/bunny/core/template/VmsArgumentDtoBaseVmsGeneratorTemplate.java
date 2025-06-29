package cn.bunny.core.template;

import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.utils.TypeConvertUtil;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用模板方法，方便扩展
 * 如果需要继承 AbstractVmsGenerator
 */
public class VmsArgumentDtoBaseVmsGeneratorTemplate extends AbstractVmsGeneratorTemplate {

    private final VmsArgumentDto dto;
    private final String path;
    private final String tableName;

    /**
     * @param dto       类名称可以自定义，格式为 xxx_xxx
     * @param path      当前路径
     * @param tableName 表名称
     */
    public VmsArgumentDtoBaseVmsGeneratorTemplate(VmsArgumentDto dto, String path, String tableName) {
        this.dto = dto;
        this.path = path;
        this.tableName = tableName;
    }

    /**
     * 添加生成内容
     *
     * @param context VelocityContext
     */
    @Override
    void addContext(VelocityContext context) {
        // 当前日期
        String date = new SimpleDateFormat(dto.getSimpleDateFormat()).format(new Date());
        context.put("date", date);

        // 作者名字
        context.put("author", dto.getAuthor());

        // 每个 Controller 上的请求前缀
        context.put("requestMapping", dto.getRequestMapping());

        // 表字段的注释内容
        context.put("comment", dto.getComment());

        // 设置包名称
        context.put("package", dto.getPackageName());

        // 将类名称转成小驼峰
        String toCamelCase = TypeConvertUtil.convertToCamelCase(tableName);
        context.put("classLowercaseName", toCamelCase);

        // 将类名称转成大驼峰
        String convertToCamelCase = TypeConvertUtil.convertToCamelCase(tableName, true);
        context.put("classUppercaseName", convertToCamelCase);
    }

    /**
     * Velocity 生成模板
     *
     * @param context VelocityContext
     * @param writer  StringWriter 写入
     */
    @Override
    void templateMerge(VelocityContext context, StringWriter writer) {
        Template servicePathTemplate = Velocity.getTemplate("vms/" + path, "UTF-8");
        servicePathTemplate.merge(context, writer);
    }
}
