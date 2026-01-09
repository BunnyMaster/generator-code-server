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
<#include "../common/common-comment.ftl">
<#include "../common/common-import.ftl">


<script lang="ts" setup>
    <@importPackage/>

    const tableRef = ref();
    const formRef = ref();
    const ${formatterTableNameByLowercaseName}Store = use${formatterTableNameByLowercaseName}StoreHook();

    /** 当前页改变时 */
    const onCurrentPageChange = async (value: number) => {
        ${formatterTableNameByLowercaseName}Store.pagination.currentPage = value;
        await onSearch();
    }

    /** 当分页发生变化 */
    const onPageSizeChange = async (value: number) => {
        ${formatterTableNameByLowercaseName}Store.pagination.pageSize = value;
        await onSearch();
    };

    /** 选择多行 */
    const onSelectionChange = (rows: Array<any>) => {
        deleteIds.value = rows.map((row: any) => row.id);
    };

    /** 重置表单 */
    const resetForm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return;
        formEl.resetFields();
        await onSearch();
    };

    onMounted(() => {
        onSearch();
    });
</script>

<template>
    <div class="main">
        <el-form ref="formRef" :inline="true" :model="${formatterTableNameByLowercaseName}Store.form"
                 class="search-form bg-bg_color w-[99/100] pl-8 pt-[12px] overflow-auto" label-width="140px"
                 label-position="right">
            <#list columnInfoList as item>
                <!-- $item.comment -->
                <el-form-item label="${item.comment}" prop="${item.lowercaseName}">
                    <el-input v-model="${formatterTableNameByLowercaseName}Store.form.${item.lowercaseName}"
                              placeholder="输入${item.comment}"
                              class="!w-[180px]" clearable/>
                </el-form-item>

            </#list>
            <el-form-item>
                <el-button :icon="useRenderIcon('ri:search-line')"
                           :loading="${formatterTableNameByLowercaseName}Store.loading"
                           type="primary"
                           @click="onSearch">
                    搜索
                </el-button>
                <el-button :icon="useRenderIcon(Refresh)" @click="resetForm(formRef)"> 重置</el-button>
            </el-form-item>
        </el-form>

        <PureTableBar :columns="columns" title="${metaData.comment}" @fullscreen="tableRef.setAdaptive()"
                      @refresh="onSearch">
            <template #buttons>
                <el-button :icon="useRenderIcon(AddFill)" type="primary" @click="onAdd"> 新增</el-button>

                <!-- 批量删除按钮 -->
                <el-button :disabled="deleteIds.length <= 0" :icon="useRenderIcon(Delete)" type="danger"
                           @click="delete${formatterTableNameByLowercaseName}s(deleteIds)">
                    批量删除
                </el-button>
            </template>

            <template v-slot="{ size, dynamicColumns }">
                <pure-table
                        ref="tableRef"
                        :adaptiveConfig="{ offsetBottom: 96 }"
                        :columns="dynamicColumns"
                        :data="${formatterTableNameByLowercaseName}Store.datalist"
                        :header-cell-style="{ background: 'var(--el-fill-color-light)', color: 'var(--el-text-color-primary)' }"
                        :loading="${formatterTableNameByLowercaseName}Store.loading"
                        :size="size"
                        adaptive
                        align-whole="center"
                        border
                        highlight-current-row
                        row-key="id"
                        showOverflowTooltip
                        table-layout="auto"
                        :pagination="${formatterTableNameByLowercaseName}Store.pagination"
                        @page-size-change="onPageSizeChange"
                        @selection-change="onSelectionChange"
                        @page-current-change="onCurrentPageChange"
                >
                    <template <#noparse>#operation</#noparse>="{ row }">
                        <el-button :icon="useRenderIcon(EditPen)" :size="size" link type="primary"
                                   @click="onUpdate(row)">
                            修改
                        </el-button>

                        <el-button :icon="useRenderIcon(Delete)" :size="size" link type="danger"
                                   @click="delete${formatterTableNameByLowercaseName}s(row)">
                            删除
                        </el-button>
                    </template>
                </pure-table>
            </template>
        </PureTableBar>
    </div>
</template>
