<#-- @ftlvariable name="date" type="java.lang.String" -->
<#-- @ftlvariable name="author" type="java.lang.String" -->
<#-- @ftlvariable name="tableName" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="commentTime" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByKebabCase" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="metaData" type="com.auth.module.generator.model.entity.meta.MetaData" -->
<#-- @ftlvariable name="columnInfoList" type="java.util.List<com.auth.module.generator.model.entity.ColumnMetaData>" -->
<#-- @ftlvariable name="tableMetaDataList" type="com.auth.module.generator.model.entity.meta.MetaData" -->

<#-- 引入包含javaComment宏的模板 -->
<#include "../common/common-comment.ftl">
<#include "../common/common-import.ftl">

<@importPackage/>

<@javaComment "${metaData.comment}", "${commentTime}", "${author}", "Mapper"/>
@Mapper
public interface ${formatterTableNameByUppercaseName}Mapper extends BaseMapper<${formatterTableNameByUppercaseName}Entity> {

    /**
     * 分页查询${metaData.comment}内容
     *
     * @param pageParams ${metaData.comment}分页参数
     * @param query ${metaData.comment}查询表单
     * @return ${metaData.comment}分页结果
     */
    IPage<${formatterTableNameByUppercaseName}PageVO> selectListByPage(@Param("page") IPage<${formatterTableNameByUppercaseName}Entity> pageParams,
                                                                       @Param("query") ${formatterTableNameByUppercaseName}Query query);

}
