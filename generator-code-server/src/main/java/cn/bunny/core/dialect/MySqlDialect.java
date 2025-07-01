package cn.bunny.core.dialect;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MySqlDialect implements DatabaseDialect {

    /**
     * 提取表注释
     *
     * @param tableOptions 选项
     * @return 注释信息
     */
    @Override
    public String extractTableComment(String tableOptions) {
        Pattern pattern = Pattern.compile("COMMENT\\s*=\\s*'(.*?)'", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(tableOptions);
        return matcher.find() ? matcher.group(1) : null;
    }

    /**
     * 提取列注释
     *
     * @param columnSpecs 列规格
     * @return 注释信息
     */
    @Override
    public String extractColumnComment(List<String> columnSpecs) {
        String columnSpecsString = String.join(" ", columnSpecs);
        Matcher columnSpecsStringMatcher = Pattern.compile("COMMENT\\s*'(.*?)'", Pattern.CASE_INSENSITIVE).matcher(columnSpecsString);
        return columnSpecsStringMatcher.find() ? columnSpecsStringMatcher.group(1) : null;
    }
}