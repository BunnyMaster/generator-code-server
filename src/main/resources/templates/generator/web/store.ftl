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

<@jsComment "${metaData.comment}Store", "${commentTime}", "${author}", "数据库表名称：${tableName}"/>
export const use${formatterTableNameByUppercaseName}Store = defineStore('${tableNameByLowercaseName}Store', {
    state() {
        return {
            // ${metaData.comment}列表
            datalist: [],
            // 查询表单
            form: {
                <#list columnInfoList as field>
                    // ${field.comment}
                    ${field.lowercaseName}: ${field.javascriptType};
                </#list>
            },
            // 分页查询结果
            pagination: {
                currentPage: 1,
                pageSize: 30,
                total: 1,
                pageSizes,
            },
            // 加载
            loading: false,
        }
    },
    getters: {},
    actions: {
        <@jsComment "获取${metaData.comment}",
        "${commentTime}",
        "${author}",""/>
        async fetch${formatterTableNameByUppercaseName}Page() {
            // 整理请求参数
            const pageParams = {pageIndex: this.pagination.currentPage, pageSize: this.pagination.pageSize};
            const data = {...pageParams, ...this.form} as any;
            delete data.pageSizes;
            delete data.total;
            delete data.background;

            // 获取${metaData.comment}列表
            const result = await query${formatterTableNameByUppercaseName}Page(data);

            // 公共页面函数hook
            const pagination = storePagination.bind(this);
            return pagination(result);
        },

        <@jsComment "查看${metaData.comment}详情",
        "${commentTime}",
        "${author}",""/>
        async get${formatterTableNameByUppercaseName}Detail(data: any) {
            try {
                const response = await get${formatterTableNameByUppercaseName}Detail(data);
                if (response.code === 200) {
                    return response.data;
                }
                return {};
            } catch (error) {
                errorMessage(error.message);
                return {};
            }
        },

        <@jsComment "添加${metaData.comment}",
        "${commentTime}",
        "${author}",""/>
        async create${formatterTableNameByUppercaseName}(data: any) {
            try {
                // 调用API获取权限列表数据
                const response = await create${formatterTableNameByUppercaseName}(data);
                return response.code === 200;
            } catch (error) {
                // 处理错误情况，显示错误信息
                errorMessage(error.message);
                return false;
            }
        },

        <@jsComment "修改${metaData.comment}",
        "${commentTime}",
        "${author}",""/>
        async update${formatterTableNameByUppercaseName}ById(data: any) {
            try {
                // 调用API获取权限列表数据
                const response = await update${formatterTableNameByUppercaseName}ById(data);
                return response.code === 200;
            } catch (error) {
                // 处理错误情况，显示错误信息
                errorMessage(error.message);
                return false;
            }
        },

        <@jsComment "删除${metaData.comment}",
        "${commentTime}",
        "${author}",""/>
        async batchDelete${formatterTableNameByUppercaseName}(data: any) {
            try {
                // 调用API获取权限列表数据
                const response = await batchDelete${formatterTableNameByUppercaseName}(data);
                return response.code === 200;
            } catch (error) {
                // 处理错误情况，显示错误信息
                errorMessage(error.message);
                return false;
            }
        },
    },
});

export function use${formatterTableNameByUppercaseName}StoreHook() {
    return use${formatterTableNameByUppercaseName}Store(store);
}