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
<#include "../../common/common-comment.ftl">
<#include "../../common/common-import.ftl">

<@importPackage/>
export const columns: TableColumnList = [
    {type: 'selection', align: 'left'},
    {type: 'index', index: (index: number) => index + 1, label: '序号', width: 60},
<#list columnInfoList as field>
    // ${field.comment}
    {label: "${field.comment}", prop: '${field.lowercaseName}'},
</#list>
    {label: "更新时间", prop: 'updateTime', sortable: true, width: 160},
    {label: "创建时间", prop: 'createTime', sortable: true, width: 160},
    {
        label: '创建用户',
        prop: 'createUser',
        width: 130,
        formatter(row: ${formatterTableNameByUppercaseName}ListItem) {
            return (
                row.createUser && (
                    <ElButton link type="primary" onClick={() => selectUserinfo(row.createUser)}>
                        {row.createUsername}
                    </ElButton>
                )
            );
        },
    },
    {
        label: '更新用户',
        prop: 'updateUser',
        width: 130,
        formatter(row: ${formatterTableNameByUppercaseName}ListItem) {
            return (
                row.updateUser && (
                    <ElButton link type="primary" onClick={() => selectUserinfo(row.updateUser)}>
                        {row.updateUsername}
                    </ElButton>
                )
            );
        },
    },
    {label: "操作", fixed: 'right', width: 220, slot: 'operation'},
];

// 添加规则
export const rules = reactive<FormRules>({
    <#list columnInfoList as field>
        // ${field.comment}
        ${field.lowercaseName}: [{required: true, message: `需要输入${field.comment}`, trigger: 'blur'}],
    </#list>
});


