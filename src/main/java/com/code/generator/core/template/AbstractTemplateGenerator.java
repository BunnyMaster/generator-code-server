package com.code.generator.core.template;


import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.TableMetaData;
import org.apache.velocity.VelocityContext;

import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板生成抽象基类
 * 定义代码生成的模板方法流程
 */
public abstract class AbstractTemplateGenerator {

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

    /**
     * 添加生成内容（由子类实现）
     */
    protected abstract void addContext(VelocityContext context);

    /**
     * 模板合并（由子类实现）
     */
    protected abstract void templateMerge(VelocityContext context, StringWriter writer);
}
