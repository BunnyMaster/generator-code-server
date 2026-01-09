<#-- 第三方包 -->
<#macro thirdPartyPackage>
    <link
            href="https://unpkg.com/element-plus/dist/index.css"
            rel="stylesheet"
    />
    <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
    <script src="https://unpkg.com/element-plus"></script>
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
</#macro>

<#-- 自己共用的包 -->
<#macro commonPackage>
    <!-- 工具类 -->
    <script src="/pages/utils/commonUtil.js"></script>
    <script src="/pages/utils/searParamsUtil.js"></script>

    <!-- 配置规则 -->
    <script src="/pages/rules/config-rules.js"></script>
</#macro>