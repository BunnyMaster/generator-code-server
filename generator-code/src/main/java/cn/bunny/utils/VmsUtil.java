package cn.bunny.utils;

import cn.bunny.dao.dto.VmsArgumentDto;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class VmsUtil {

    /**
     * 生成模板
     *
     * @param writer       写入字符串内容
     * @param context      Velocity上下文
     * @param templateName 模板名称
     * @param dto          类名称可以自定义，格式为 xxx_xxx
     */
    public static void commonVms(StringWriter writer, VelocityContext context, String templateName, VmsArgumentDto dto) {
        // 类名称如果是小驼峰，需要 [手写] 为 [下划线] 之后由 [代码 -> 小驼峰/大驼峰]
        String className = dto.getClassName();

        // 去除表开头前缀
        String tablePrefixes = dto.getTablePrefixes();

        // 当前捉着
        String author = dto.getAuthor();

        // 每个 Controller 上的请求前缀
        String requestMapping = dto.getRequestMapping();

        // 将 表前缀  转成数组
        AtomicReference<String> replaceTableName = new AtomicReference<>(className);
        for (String prefix : tablePrefixes.split("[,，]")) {
            replaceTableName.set(className.replace(prefix, ""));
        }

        String date = new SimpleDateFormat(dto.getSimpleDateFormat()).format(new Date());
        // vm 不能直接写 `{` 需要转换下
        context.put("leftBrace", "{");

        // 当前日期
        context.put("date", date);

        // 作者名字
        context.put("author", author);

        // 每个 Controller 上的请求前缀
        context.put("requestMapping", requestMapping);

        // 将类名称转成小驼峰
        context.put("classLowercaseName", ConvertUtil.convertToCamelCase(replaceTableName.get()));

        // 将类名称转成大驼峰
        context.put("classUppercaseName", ConvertUtil.convertToCamelCase(replaceTableName.get(), true));

        // Velocity 生成模板
        Template servicePathTemplate = Velocity.getTemplate(templateName, "UTF-8");
        servicePathTemplate.merge(context, writer);
    }
}
