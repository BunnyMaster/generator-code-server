<#-- @ftlvariable name="user" type="java.lang.String" -->
<#-- @ftlvariable name="latestProduct" type="java.util.Map<String, String>" -->

<html lang="zh">
<head>
    <title>Welcome!</title>
</head>
<body>
<h1>Welcome ${user}!</h1>
<p>Our latest product:
    <a href="${latestProduct.url}">${latestProduct.name}</a>!
</body>
</html>