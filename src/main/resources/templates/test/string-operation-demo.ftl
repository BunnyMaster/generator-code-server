<#-- @ftlvariable name="user" type="java.lang.String" -->

<#assign s = "Hello ${user}!">
${s}

<#-- 也可以使用 + 号来达到类似的效果 -->
<#assign s = "Hello +" + user + "!">
${s}

<#-- 获取字符 -->
${user[0]}
${user[2]}

<#-- 字符串切分 (子串) -->
<#assign s = "ABCDEF">
${s[2..3]}
${s[2..<4]}
${s[2..*3]}
${s[2..*100]}
${s[2..]}