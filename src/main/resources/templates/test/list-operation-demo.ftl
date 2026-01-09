<#list ["Joe", "Fred"] + ["Julia", "Kate"] as user>
    - ${user}
</#list>

<#assign seq = ["A", "B", "C", "D", "E"]>
<#list seq[1..3] as i>${i}</#list>

<#assign seq = ["A", "B", "C"]>

Slicing with length limited ranges:
- <#list seq[0..*2] as i>${i}</#list>
- <#list seq[1..*2] as i>${i}</#list>
- <#list seq[2..*2] as i>${i}</#list> <#-- Not an error -->
- <#list seq[3..*2] as i>${i}</#list> <#-- Not an error -->

Slicing with right-unlimited ranges:
- <#list seq[0..] as i>${i}</#list>
- <#list seq[1..] as i>${i}</#list>
- <#list seq[2..] as i>${i}</#list>
- <#list seq[3..] as i>${i}</#list>