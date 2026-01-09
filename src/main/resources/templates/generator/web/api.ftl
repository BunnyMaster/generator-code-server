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

<@jsComment "获取${metaData.comment}列表",
"${commentTime}",
"${author}",
""/>
export const query${formatterTableNameByUppercaseName}Page = (params: any) => {
    return http.request<ResultPage<${formatterTableNameByUppercaseName}ListItem>>('get', `${requestPrefix}/${formatterTableNameByKebabCase}`, {params});
};

<@jsComment "获取${metaData.comment}详情",
"${commentTime}",
"${author}",
""/>
export const get${formatterTableNameByUppercaseName}Detail = (params: any) => {
    return http.request<Result<${formatterTableNameByUppercaseName}ListItem>>("get", `${requestPrefix}/${formatterTableNameByKebabCase}/<#noparse>${params.id}</#noparse>`);
};

<@jsComment "添加${metaData.comment}",
"${commentTime}",
"${author}",
""/>
export const create${formatterTableNameByUppercaseName} = (data: any) => {
    return http.request<Result<string>>('post', '${requestPrefix}/${formatterTableNameByKebabCase}', {data});
};

<@jsComment "更新${metaData.comment}",
"${commentTime}",
"${author}",
""/>
export const update${formatterTableNameByUppercaseName}ById = (data: any) => {
    return http.request<Result<string>>('put', '${requestPrefix}/${formatterTableNameByKebabCase}', {data});
};

<@jsComment "删除${metaData.comment}",
"${commentTime}",
"${author}",
""/>
export const batchDelete${formatterTableNameByUppercaseName} = (data: any) => {
    return http.requests<Result<string>>('delete', '${requestPrefix}/${formatterTableNameByKebabCase}', {data});
};