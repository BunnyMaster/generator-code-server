<#-- @ftlvariable name="date" type="java.lang.String" -->
<#-- @ftlvariable name="author" type="java.lang.String" -->
<#-- @ftlvariable name="tableName" type="java.lang.String" -->
<#-- @ftlvariable name="packagePath" type="java.lang.String" -->
<#-- @ftlvariable name="requestPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="tableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByUppercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="formatterTableNameByLowercaseName" type="java.lang.String" -->
<#-- @ftlvariable name="metaData" type="com.auth.module.generator.model.entity.meta.MetaData" -->
<#-- @ftlvariable name="columnInfoList" type="java.util.List<com.auth.module.generator.model.entity.ColumnMetaData>" -->
<#-- @ftlvariable name="field" type="com.auth.module.generator.model.entity.meta.ColumnMetaData" -->
<#-- @ftlvariable name="tableMetaDataList" type="com.auth.module.generator.model.entity.meta.MetaData" -->

<#-- 引入包含javaComment宏的模板 -->
<#include "../common/common-comment.ftl">
<#include "../common/common-import.ftl">

<@importPackage/>

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("${tableName}")
@Schema(name = "${formatterTableNameByUppercaseName}Entity", title = "${metaData.comment}", description = "${metaData.comment}实体类")
public class ${formatterTableNameByUppercaseName}Entity extends BaseEntity {

<#list columnInfoList as field>
    <#if field.isPrimaryKey>
        @TableId(type = IdType.ASSIGN_ID)
    </#if>
    @Schema(name = "${field.lowercaseName}", title = "${field.comment}")
    private ${field.javaType} ${field.lowercaseName};

</#list>
}