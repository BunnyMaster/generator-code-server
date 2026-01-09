package com.auth.module.generator.core.provider.metadata.impl;

import com.alibaba.fastjson2.JSON;
import com.auth.module.generator.core.provider.builder.TemplateDataModelBuilder;
import com.auth.module.generator.core.provider.dialect.GeneratorCategoryDialect;
import com.auth.module.generator.core.provider.metadata.AbstractDatabaseMetadataProvider;
import com.auth.module.generator.core.provider.validate.ValidationService;
import com.auth.module.generator.exception.GeneratorCodeException;
import com.auth.module.generator.exception.SqlParseException;
import com.auth.module.generator.model.dto.GenerationConfigDTO;
import com.auth.module.generator.model.entity.generator.extra.SQLConfigEntity;
import com.auth.module.generator.model.entity.meta.ColumnMetaData;
import com.auth.module.generator.model.entity.meta.MetaData;
import com.auth.module.generator.model.value.TemplateDataModel;
import com.auth.module.generator.model.vo.ElementOptionVO;
import com.auth.module.generator.utils.NameConvertUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.List;

@Component
public class SQLParserMetadataProvider extends AbstractDatabaseMetadataProvider {
    /**
     * 表标识字段
     */
    public static final String TABLE = "TABLE";

    private final GeneratorCategoryDialect mySqlDialect;

    private final TemplateDataModelBuilder<SQLConfigEntity> templateDataModelBuilder;

    public SQLParserMetadataProvider(@Qualifier("mySQLDialect") GeneratorCategoryDialect mySqlDialect,
                                     FreeMarkerConfigurer freeMarkerConfigurer,
                                     TemplateDataModelBuilder<SQLConfigEntity> templateDataModelBuilder,
                                     ValidationService validationService) {
        super(freeMarkerConfigurer, validationService);
        this.mySqlDialect = mySqlDialect;
        this.templateDataModelBuilder = templateDataModelBuilder;
    }

    /**
     * 获取元数据提供器的类型
     *
     * @return supportProvider 元数据提供器的类型
     */
    @Override
    public ElementOptionVO getProviderType() {
        return ElementOptionVO.builder().value("MySQLParser").label("MySQL 语句解析").build();
    }

    /**
     * 解析 sql 表信息
     * 先解析SQL语句，解析列字段信息
     *
     * @param sqlStatement sql 语句
     * @return 表西悉尼
     * @see CCJSqlParserUtil 使用这个工具进行SQL 的解析
     */
    @Override
    public MetaData getMetadata(String sqlStatement) {
        MetaData tableInfo = new MetaData();

        // 解析 sql
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sqlStatement);
        } catch (JSQLParserException e) {
            throw new GeneratorCodeException("SQL 解析失败");
        }

        if (!(statement instanceof CreateTable createTable)) {
            throw new IllegalArgumentException("缺少SQL 语句");
        }

        // 设置表基本信息
        String tableName = createTable.getTable().getName().replace("`", "");
        tableInfo.setName(tableName);
        tableInfo.setType(TABLE);
        String tableOptionsStrings = String.join(" ", createTable.getTableOptionsStrings());

        // 注释信息
        String comment = mySqlDialect.extractTableComment(tableOptionsStrings);
        tableInfo.setComment(comment);

        return tableInfo;
    }

    /**
     * 获取当前表的列属性
     *
     * @param sqlStatement sql 语句
     * @return 当前表所有的列内容
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String sqlStatement) {
        // 解析 sql
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sqlStatement);
        } catch (JSQLParserException e) {
            throw new SqlParseException("Fail parse sql", e.getCause());
        }

        if (!(statement instanceof CreateTable createTable)) {
            throw new IllegalArgumentException("Lack of Sql Statement");
        }

        return createTable.getColumnDefinitions()
                .stream()
                .map(column -> {
                    // 列信息
                    ColumnMetaData columnInfo = new ColumnMetaData();

                    // 列名称
                    String columnName = column.getColumnName().replace("`", "");
                    columnInfo.setColumnName(columnName);

                    // 设置 JDBC 类型
                    String dataType = column.getColDataType().getDataType();
                    columnInfo.setJdbcType(dataType);

                    // 设置 Java 类型
                    String javaType = mySqlDialect.convertToJavaType(dataType);
                    columnInfo.setJavaType(javaType);

                    // 设置 JavaScript 类型
                    columnInfo.setJavascriptType(mySqlDialect.convertToJavaScriptType(javaType));

                    // 列字段转成 下划线 -> 小驼峰
                    String lowercaseName = NameConvertUtil.lowerCamelCase(columnName);
                    columnInfo.setLowercaseName(lowercaseName);

                    // 列字段转成 下划线 -> 大驼峰名称
                    String uppercaseName = NameConvertUtil.upperCamelCase(columnName);
                    columnInfo.setUppercaseName(uppercaseName);

                    // 解析注释
                    List<String> columnSpecs = column.getColumnSpecs();

                    // 设置列属性信息
                    String comment = mySqlDialect.extractColumnComment(columnSpecs);
                    columnInfo.setComment(comment);

                    // 设置是否是主键
                    columnInfo.setIsPrimaryKey(false);
                    return columnInfo;
                })
                .toList();
    }

    /**
     * 获取模板数据模型列表
     *
     * @param dto 生成配置DTO对象，包含SQL配置实体信息
     * @return 模板数据模型列表
     */
    @Override
    public List<TemplateDataModel> getTemplateDataModels(GenerationConfigDTO<Object> dto) {
        // 解析额外配置信息为SQL 配置实体
        SQLConfigEntity extra = JSON.parseObject(JSON.toJSONString(dto.getExtra()), SQLConfigEntity.class);
        GenerationConfigDTO<SQLConfigEntity> configDTO = new GenerationConfigDTO<>();
        BeanUtils.copyProperties(dto, configDTO);

        // 获取SQL 语句
        String sql = extra.getSql();

        // 构建并返回模板数据模型列表
        MetaData metadata = getMetadata(sql);
        List<ColumnMetaData> columnInfoList = getColumnInfoList(sql);
        return templateDataModelBuilder.templateDataModelList(configDTO, metadata, columnInfoList);
    }
}