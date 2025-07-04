package cn.bunny.core.template;

import cn.bunny.domain.dto.VmsArgumentDto;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.utils.MysqlTypeConvertUtil;
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
public class VmsTBaseTemplateGenerator extends AbstractTemplateGenerator {

    private final VmsArgumentDto dto;
    private final String path;
    private final TableMetaData tableMetaData;

    /**
     * @param dto           类名称可以自定义，格式为 xxx_xxx
     * @param path          当前路径
     * @param tableMetaData 表名称
     */
    public VmsTBaseTemplateGenerator(VmsArgumentDto dto, String path, TableMetaData tableMetaData) {
        this.dto = dto;
        this.path = path;
        this.tableMetaData = tableMetaData;

        // 处理表名称，替换前缀
        String tableName = tableMetaData.getTableName();
        String[] prefixes = dto.getTablePrefixes().split("[,，]");
        for (String prefix : prefixes) {
            if (tableName.startsWith(prefix)) {
                String handlerTableName = tableName.replace(prefix, "");
                tableMetaData.setCleanTableName(handlerTableName);
            }
        }
    }

    /**
     * 添加生成内容
     *
     * @param context VelocityContext
     */
    @Override
    public void addContext(VelocityContext context) {
        // 当前的表名
        String handlerTableName = tableMetaData.getCleanTableName();
        // 表的注释内容
        String comment = tableMetaData.getComment();

        // 当前日期
        String date = new SimpleDateFormat(dto.getSimpleDateFormat()).format(new Date());
        context.put("date", date);

        // 作者名字
        context.put("author", dto.getAuthor());

        // 每个 Controller 上的请求前缀
        context.put("requestMapping", dto.getRequestMapping());

        // 表字段的注释内容
        context.put("comment", comment);

        // 设置包名称
        context.put("package", dto.getPackageName());

        // 将类名称转成小驼峰
        String lowerCamelCase = MysqlTypeConvertUtil.convertToCamelCase(handlerTableName, false);
        context.put("classLowercaseName", lowerCamelCase);

        // 将类名称转成大驼峰
        String upperCameCase = MysqlTypeConvertUtil.convertToCamelCase(handlerTableName, true);
        context.put("classUppercaseName", upperCameCase);
    }

    /**
     * Velocity 生成模板
     *
     * @param context VelocityContext
     * @param writer  StringWriter 写入
     */
    @Override
    public void templateMerge(VelocityContext context, StringWriter writer) {
        Template servicePathTemplate = Velocity.getTemplate("vms/" + path, "UTF-8");
        servicePathTemplate.merge(context, writer);
    }
}
