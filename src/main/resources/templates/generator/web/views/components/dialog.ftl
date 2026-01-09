import React from "react";
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

<script lang="ts" setup>
    const props = withDefaults(defineProps<FormInline<${formatterTableNameByUppercaseName}Form>>(), {
        formInline: () => ({
            <#list columnInfoList as field>
            // ${field.comment}
            ${field.lowercaseName}: undefined
            </#list>
        }),
    });

    const formRef = ref<FormInstance>();
    const form = ref(props.formInline);

    defineExpose({formRef});
</script>

<template>
    <el-form ref="formRef"
             :model="form" :rules="rules" label-width="auto" label-position="left">
        <el-row :gutter="14">
            <#list columnInfoList as field>
                <!-- ${field.comment} -->
                <el-col :lg="6" :md="8" :sm="12" :xs="24">
                    <el-form-item label="${field.comment}" prop="${field.lowercaseName}">
                        <el-input v-model="form.${field.lowercaseName}" autocomplete="off" type="text"
                                  placeholder="输入${field.comment}"/>
                    </el-form-item>
                </el-col>
            </#list>
        </el-row>
    </el-form>
</template>
