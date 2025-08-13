package com.code.generator.core.provider;


import com.code.generator.core.dialect.DatabaseDialect;
import com.code.generator.exception.GeneratorCodeException;
import com.code.generator.exception.SqlParseException;
import com.code.generator.model.entity.ColumnMetaData;
import com.code.generator.model.entity.TableMetaData;
import com.code.generator.utils.CamelCaseNameConvertUtil;
import lombok.RequiredArgsConstructor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SqlMetadataProvider implements IMetadataProvider {

    private final DatabaseDialect dialect;

    /**
     * 解析 sql 表信息
     * 先解析SQL语句，解析列字段信息
     *
     * @param sqlStatement sql语句
     * @return 表西悉尼
     * @see CCJSqlParserUtil 使用这个工具进行SQL的解析
     */
    @Override
    public TableMetaData getTableMetadata(String sqlStatement) {
        TableMetaData tableInfo = new TableMetaData();

        // 解析sql
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(sqlStatement);
        } catch (JSQLParserException e) {
            throw new GeneratorCodeException("SQL解析失败");
        }

        if (!(statement instanceof CreateTable createTable)) {
            throw new IllegalArgumentException("缺少SQL语句");
        }

        // 设置表基本信息
        String tableName = createTable.getTable().getName().replaceAll("`", "");
        tableInfo.setTableName(tableName);
        tableInfo.setTableType("TABLE");
        String tableOptionsStrings = String.join(" ", createTable.getTableOptionsStrings());

        // 注释信息
        String comment = dialect.extractTableComment(tableOptionsStrings);
        tableInfo.setComment(comment);

        return tableInfo;
    }

    /**
     * 获取当前表的列属性
     *
     * @param sqlStatement sql语句
     * @return 当前表所有的列内容
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String sqlStatement) {
        // 解析sql
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
                .stream().map(column -> {
                    // 列信息
                    ColumnMetaData columnInfo = new ColumnMetaData();

                    // 列名称
                    String columnName = column.getColumnName().replaceAll("`", "");
                    columnInfo.setColumnName(columnName);

                    // 设置 JDBC 类型
                    String dataType = column.getColDataType().getDataType();
                    columnInfo.setJdbcType(dataType);

                    // 设置 Java 类型
                    String javaType = dialect.convertToJavaType(dataType.contains("varchar") ? "varchar" : dataType);
                    columnInfo.setJavaType(javaType);

                    // 设置 JavaScript 类型
                    columnInfo.setJavascriptType(dialect.convertToJavaScriptType(javaType));

                    // 列字段转成 下划线 -> 小驼峰
                    String lowercaseName = CamelCaseNameConvertUtil.convertToCamelCase(columnName, false);
                    columnInfo.setLowercaseName(lowercaseName);

                    // 列字段转成 下划线 -> 大驼峰名称
                    String uppercaseName = CamelCaseNameConvertUtil.convertToCamelCase(columnName, true);
                    columnInfo.setUppercaseName(uppercaseName);

                    // 解析注释
                    List<String> columnSpecs = column.getColumnSpecs();

                    // 设置列属性信息
                    String comment = dialect.extractColumnComment(columnSpecs);
                    columnInfo.setComment(comment);

                    return columnInfo;
                }).toList();
    }
}
