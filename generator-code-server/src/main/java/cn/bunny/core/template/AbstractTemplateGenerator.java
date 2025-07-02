package cn.bunny.core.template;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板方法模式
 * 如果需要继承 AbstractVmsGenerator
 */
public abstract class AbstractTemplateGenerator {

    /**
     * 添加生成内容
     */
    protected abstract void addContext(VelocityContext context);

    /**
     * Velocity 生成模板
     *
     * @param context VelocityContext
     * @param writer  StringWriter 写入
     */
    protected abstract void templateMerge(VelocityContext context, StringWriter writer);

    /**
     * 生成代码模板
     *
     * @param tableMeta 表元数据
     * @param columns   列信息列表
     * @return 生成的代码内容
     */
    public StringWriter generateCode(TableMetaData tableMeta, List<ColumnMetaData> columns) {
        VelocityContext context = new VelocityContext();
        prepareVelocityContext(context, tableMeta, columns);
        return mergeTemplate(context);
    }

    /**
     * 准备Velocity上下文数据
     */
    private void prepareVelocityContext(VelocityContext context, TableMetaData tableMeta, List<ColumnMetaData> columns) {
        // 特殊字符处理
        context.put("leftBrace", "{");
        context.put("tableName", tableMeta.getTableName());
        context.put("columnInfoList", columns);
        context.put("baseColumnList", getDistinctColumnNames(columns));
        addContext(context);  // 子类可扩展
    }

    /**
     * 获取去重的列名列表
     */
    private String getDistinctColumnNames(List<ColumnMetaData> columns) {
        return columns.stream()
                .map(ColumnMetaData::getColumnName)
                .distinct()
                .collect(Collectors.joining(","));
    }

    /**
     * 合并Velocity模板
     */
    private StringWriter mergeTemplate(VelocityContext context) {
        StringWriter writer = new StringWriter();
        templateMerge(context, writer);
        return writer;
    }

}
