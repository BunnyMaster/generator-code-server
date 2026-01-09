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


export const formRef = ref();
const ${formatterTableNameByUppercaseName}Store = use${formatterTableNameByUppercaseName}Store();

<@jsComment "$搜索初始化", "${commentTime}", "${author}", ""/>
export async function onSearch() {
    try {
        ${formatterTableNameByUppercaseName}Store.loading = true;

        await ${formatterTableNameByUppercaseName}Store.fetch${formatterTableNameByUppercaseName}Page();
    } finally {
        // 数据加载完成后，设置加载状态为false
        ${formatterTableNameByUppercaseName}Store.loading = false;
    }
}

<@jsComment "添加${metaData.comment}", "${commentTime}", "${author}", ""/>
export function onAdd() {
    addDialog({
        title: `添加${metaData.comment}`,
        props: {formInline: {}},
        draggable: true,
        fullscreenIcon: true,
        closeOnClickModal: false,
        contentRenderer: () => h(${formatterTableNameByUppercaseName}Dialog, {ref: formRef}),
        beforeSure: (done, {options}) => {
            const form = options.props.formInline as FormItemProps;
            formRef.value.formRef.validate(async (valid: any) => {
                if (!valid) return;

                const result = await ${formatterTableNameByUppercaseName}Store.create${formatterTableNameByUppercaseName}(form);
                if (!result) return;
                done();
                await onSearch();
            });
        },
    });
}

<@jsComment "更新${metaData.comment}", "${commentTime}", "${author}", ""/>
export function onUpdate(row: any) {
    addDialog({
        title: `修改${metaData.comment}`,
        props: {formInline: {...row}},
        draggable: true,
        fullscreenIcon: true,
        closeOnClickModal: false,
        contentRenderer: () => h(${formatterTableNameByUppercaseName}Dialog, {ref: formRef}),
        beforeSure: (done, {options}) => {
            const form = options.props.formInline as FormItemProps;
            formRef.value.formRef.validate(async (valid: any) => {
                if (!valid) return;

                const result = await ${formatterTableNameByUppercaseName}Store.update${formatterTableNameByUppercaseName}ById({
                    ...form,
                    id: row.id
                });
                if (!result) return;
                done();
                await onSearch();
            });
        },
    });
}

/**
 * 删除用户信息
 * @param ids 要删除的用户ID数组，如果传单个对象会自动提取其id
 *  使用示例：
 *  删除单个: delete${formatterTableNameByUppercaseName}s(row)
 *  批量删除: delete${formatterTableNameByUppercaseName}s(deleteIds.value)
 */
export const delete${formatterTableNameByUppercaseName}s = async (ids: any[] | any) => {
    // 处理参数：如果是单个对象，提取其id；如果是数组，直接使用
    const idArray = Array.isArray(ids) ? ids : [ids?.id];

    const result = await messageBoxSecondConfirm();
    if (!result) return;

    // 删除数据
    await ${formatterTableNameByUppercaseName}Store.batchDelete${formatterTableNameByUppercaseName}(idArray);
    await onSearch();
};