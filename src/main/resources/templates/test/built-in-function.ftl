<#-- @ftlvariable name="testString" type="java.lang.String" -->
<#-- @ftlvariable name="testSequence" type="java.util.List" -->

${testString?upper_case}
${testString?html}
${testString?upper_case?html}

${testSequence?size}
${testSequence?join(", ")}