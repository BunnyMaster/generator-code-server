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
<#-- @ftlvariable name="field" type="com.auth.module.generator.model.entity.meta.ColumnMetaData" -->
<#-- @ftlvariable name="tableMetaDataList" type="com.auth.module.generator.model.entity.meta.MetaData" -->

<#-- 引入包含javaComment宏的模板 -->
<#include "common/common-comment.ftl">
<#include "common/common-import.ftl">

<@importPackage />

<@jsComment "${metaData.comment}", "${commentTime}", "${author}", "数据库表名称：${tableName}"/>
export interface ${formatterTableNameByUppercaseName}ListItem extends BaseModel {
<#list columnInfoList as field>
    // ${field.comment}
    ${field.lowercaseName}: ${field.javascriptType};

</#list>
}

<@jsComment "${metaData.comment}提交表单", "${commentTime}", "${author}", "数据库表名称：${tableName}"/>
export interface ${formatterTableNameByUppercaseName}Form {

<#list columnInfoList as field>
    // ${field.comment}
    ${field.lowercaseName}: ${field.javascriptType};

</#list>
}

<@jsComment "${metaData.comment}详情", "${commentTime}", "${author}", "数据库表名称：${tableName}"/>
export interface ${formatterTableNameByUppercaseName}Info {
<#list columnInfoList as field>
    // ${field.comment}
    ${field.lowercaseName}: ${field.javascriptType};

</#list>
}