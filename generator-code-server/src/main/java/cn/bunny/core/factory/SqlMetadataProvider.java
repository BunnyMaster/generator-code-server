package cn.bunny.core.factory;

import cn.bunny.domain.entity.ColumnMetaData;
import cn.bunny.domain.entity.TableMetaData;
import cn.bunny.exception.GeneratorCodeException;
import cn.bunny.utils.TypeConvertUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SqlMetadataProvider implements IMetadataProvider {

    /**
     * 解析 sql 表信息
     * 先解析SQL语句，解析列字段信息
     *
     * @param identifier 表名称或sql
     * @return 表西悉尼
     * @see CCJSqlParserUtil 使用这个工具进行SQL的解析
     */
    @Override
    public TableMetaData getTableMetadata(String identifier) {
        TableMetaData tableInfo = new TableMetaData();

        // 解析sql
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(identifier);
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
        Pattern pattern = Pattern.compile("COMMENT\\s*=\\s*'(.*?)'", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tableOptionsStrings);
        if (matcher.find()) {
            tableInfo.setComment(matcher.group(1));
        }

        return tableInfo;
    }

    /**
     * 获取当前表的列属性
     *
     * @param identifier 表名称或sql
     * @return 当前表所有的列内容
     */
    @Override
    public List<ColumnMetaData> getColumnInfoList(String identifier) {
        // 解析sql
        Statement statement;
        try {
            statement = CCJSqlParserUtil.parse(identifier);
        } catch (JSQLParserException e) {
            throw new RuntimeException("SQL解析失败");
        }

        if (!(statement instanceof CreateTable createTable)) {
            throw new IllegalArgumentException("缺少SQL语句");
        }

        return createTable.getColumnDefinitions()
                .stream().map(column -> {
                    // 列信息
                    ColumnMetaData columnInfo = new ColumnMetaData();

                    // 列名称
                    columnInfo.setColumnName(column.getColumnName());

                    // 设置 JDBC 类型
                    String dataType = column.getColDataType().getDataType();
                    columnInfo.setJdbcType(dataType);

                    // 设置 Java 类型
                    String javaType = TypeConvertUtil.convertToJavaType(dataType.contains("varchar") ? "varchar" : dataType);
                    columnInfo.setJavaType(javaType);

                    // 设置 JavaScript 类型
                    columnInfo.setJavascriptType(StringUtils.uncapitalize(javaType));

                    // 列字段转成 下划线 -> 小驼峰
                    columnInfo.setLowercaseName(TypeConvertUtil.convertToCamelCase(column.getColumnName()));
                    // 列字段转成 下划线 -> 大驼峰名称
                    columnInfo.setUppercaseName(TypeConvertUtil.convertToCamelCase(column.getColumnName(), true));

                    // 解析注释
                    List<String> columnSpecs = column.getColumnSpecs();
                    String columnSpecsString = String.join(" ", columnSpecs);
                    Matcher columnSpecsStringMatcher = Pattern.compile("COMMENT\\s*'(.*?)'", Pattern.CASE_INSENSITIVE).matcher(columnSpecsString);
                    if (columnSpecsStringMatcher.find()) {
                        columnInfo.setComment(columnSpecsStringMatcher.group(1));
                    }

                    return columnInfo;
                }).toList();
    }
}
