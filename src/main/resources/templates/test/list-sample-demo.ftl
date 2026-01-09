<#-- @ftlvariable name="animals" type="java.util.List<freemarker.ListSampleTest.Animal>" -->
<#-- @ftlvariable name="animal" type="java.util.Map" -->

<p>We have these animals:
<table border=1>
    <#list animals as animal>
        <tr>
            <td>${animal.name}</td>
            <td>${animal.price} Euros</td>
        </tr>
    </#list>
</table>