<#if price == 0>
    Pythons are free today!
</#if>

<#if price != 0>
    Pythons are not free today!
</#if>

<#if price < otherPrice>
    Pythons are cheaper than elephants today.
</#if>

<#if price < otherPrice>
    Pythons are cheaper than elephants today.
<#else>
    Pythons are not cheaper than elephants today.
</#if>

<#if price < otherPrice>
    Pythons are cheaper than elephants today.
<#elseif otherPrice < price>
    Elephants are cheaper than pythons today.
<#else>
    Elephants and pythons cost the same today.
</#if>

<#if flag>
    Pythons are protected animals!
</#if>