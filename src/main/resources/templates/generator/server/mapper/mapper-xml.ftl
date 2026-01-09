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

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packagePath}.mapper.${formatterTableNameByUppercaseName}Mapper">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${packagePath}.model.vo.${formatterTableNameByUppercaseName}PageVO">
        <#list columnInfoList as field>
            <id column="${field.columnName}" property="${field.lowercaseName}"/>
        </#list>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        <#list columnInfoList as field>
            ${field.columnName},
        </#list>
    </sql>

    <!-- 分页查询${metaData.comment}内容 -->
    <select id="selectListByPage" resultType="${packagePath}.model.vo.${formatterTableNameByUppercaseName}PageVO">
        select
        <#list columnInfoList as field>
            ${field.columnName}<#if field_has_next>,</#if>
        </#list>
        from $tableName
        <where>
            is_deleted = 0
            <#list columnInfoList as field>
                <if test="query.${field.lowercaseName} != null and query.${field.lowercaseName} != ''">
                    and ${field.columnName} = <#noparse>#{query.</#noparse>${field.lowercaseName}<#noparse>}</#noparse>
                </if>
            </#list>
        </where>
    </select>

</mapper>
