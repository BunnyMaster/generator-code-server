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

<#macro importPackage>
    import { onMounted, ref, reactive, h } from 'vue';
    import { FormInstance, FormRules, FormItemProps, ElAvatar, ElButton } from 'element-plus';
    import { defineStore } from 'pinia';
    import { http } from "@/utils/http";
    import { addDialog } from '@/components/ReDialog/index';
    import { message, messageBox } from '@/utils/message';
    import { errorMessage } from "@/utils/base/message";
    import { messageBoxSecondConfirm } from '@/utils/base/dialog';
    import { storePagination } from '@/utils/base/resultUtil';
    import { pageSizes } from '@/constant/PaginationConstant';
    import { store } from '@/store';
    import { deleteIds, onSearch } from './utils/actions';
    import { rules } from '../utils/${formatterTableNameByKebabCase}-columns';
    import { selectUserinfo } from '@/components/RePureTableBar/Userinfo/columns';
    import DeleteBatchDialog from "@/components/RePureTableBar/DeleteBatchDialog.vue";
    import ${formatterTableNameByUppercaseName}Dialog from '../components/${formatterTableNameByKebabCase}-dialog.vue';
    import { use${formatterTableNameByUppercaseName}Store } from '@/store/modules${requestPrefix}/${formatterTableNameByLowercaseName}';
    import { batchDelete${formatterTableNameByUppercaseName}, create${formatterTableNameByUppercaseName} } from '@/api';
    import type {
    BaseModel,
    ResultPage,
    Result,
    FormInline
    } from "@/types/base";
    import type {
    ${formatterTableNameByUppercaseName}Form,
    ${formatterTableNameByUppercaseName}ListItem,
    ${formatterTableNameByUppercaseName}Info,
    ${formatterTableNameByLowercaseName}ListItem
    } from "@/types${requestPrefix}/modules/${formatterTableNameByUppercaseName}Type";
</#macro>