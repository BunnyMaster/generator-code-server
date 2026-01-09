<#macro javaComment comment date author extra="">
    /**
    * ${comment}
    *
    * @author ${author}
    * @date ${date}
    * <#if extra?has_content>@description ${extra}</#if>
    */
</#macro>