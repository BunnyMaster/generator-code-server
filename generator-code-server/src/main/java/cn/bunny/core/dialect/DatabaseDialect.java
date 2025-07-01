package cn.bunny.core.dialect;

import java.util.List;

/**
 * 数据库方言
 */
public interface DatabaseDialect {

    /**
     * 提取表注释
     *
     * @param tableOptions 选项
     * @return 注释信息
     */
    String extractTableComment(String tableOptions);

    /**
     * 提取列注释
     *
     * @param columnSpecs 列规格
     * @return 注释信息
     */
    String extractColumnComment(List<String> columnSpecs);

}