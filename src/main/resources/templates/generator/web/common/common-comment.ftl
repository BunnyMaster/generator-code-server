<#-- @ftlvariable name="metaData" type="com.auth.module.generator.model.entity.meta.MetaData" -->
<#-- @ftlvariable name="commentTime" type="java.lang.String" -->

<#macro jsComment comment date author extra="">
    /**
    * ${comment!"系统生成"}
    *
    * @author ${author}
    * @date ${date}
    * <#if extra?has_content>@description ${extra}</#if>
    */
</#macro>