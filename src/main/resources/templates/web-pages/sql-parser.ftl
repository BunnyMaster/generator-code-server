<#include "./common/reference.ftl" />

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8"/>
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <@thirdPartyPackage />
    <title>代码生成器配置</title>
</head>

<body>
<div id="app">
    <el-row class="w-[1200px] m-auto p-4 flex flex-col">
        <h1 class="text-center text-3xl font-black mb-8"><a href="/">生成器配置</a></h1>

        <el-tabs type="border-card" v-model="activeTab" @tab-change="onTabChange">
            <!-- 通用配置 -->
            <el-tab-pane label="通用配置" name="common">
                <common-config v-model:form="configForm" v-model:provider-from="providerFrom" ref="databaseConfigRef">
                    <!-- 指定生成的表名称 -->
                    <el-form-item label="指定生成的表名称" prop="sql">
                        <el-input v-model="configForm.extra.sql" class="tag-input" type="textarea" clearable
                                  :autosize="{ minRows: 6 }" placeholder="请输入SQL语句"/>
                    </el-form-item>
                </common-config>
            </el-tab-pane>

            <el-tab-pane v-if="Object.keys(configForm.typeMap).length > 0"
                         v-for="typeMap in Object.entries(configForm.typeMap).map(([key, value]) => ({key, ...value}))"
                         :key="typeMap.key" :label="typeMap.displayName" :name="typeMap.key">
                <type-config ref="typeConfigRef" v-model:form="configForm.typeMap[typeMap.key]"
                             :type-map-key="typeMap.key"/>
            </el-tab-pane>
        </el-tabs>

        <!-- 操作按钮 -->
        <div class="text-center mt-8">
            <el-button-group>
                <el-button @click="onSubmit" size="large" type="success">
                    开始生成
                </el-button>
            </el-button-group>

            <el-button @click="resetAllForms" size="large">重置全部</el-button>
            <el-button @click="loadDefaultConfig" size="large">
                加载默认配置
            </el-button>
        </div>

        <!-- 配置预览 -->
        <config-preview :form="configForm" :generation-strategy-from="providerFrom"
                        :generator-views="generatorViews"/>
</div>
</body>

<@commonPackage />
<!-- 配置组件 -->
<script src="/pages/components/common-config.js"></script>
<script src="/pages/components/type-config.js"></script>
<script src="/pages/components/template-config.js"></script>
<script src="/pages/components/config-preview.js"></script>

<script>
    const {
        createApp, ref, reactive, computed, provide, inject, onMounted, updated, watch, toRaw, nextTick
    } = Vue;
    const {ElMessageBox} = ElementPlus;

    const app = createApp({
        setup() {
            // 组件引用
            const databaseConfigRef = ref();
            const typeConfigRef = ref();

            // 当前的tab表单，动态改变
            const activeTab = ref("common");
            // 新的表名称
            const newTableName = ref('');
            // 完整的表单数据结构
            const configForm = reactive({
                extra: {},
                typeMap: {}
            });
            // 支持哪些策略
            const providerFrom = reactive({provider: "MySQLParser", mode: "print"});

            // 生成的内容列表
            const generatorViews = ref([]);

            /**
             * 提交所有配置以进行代码生成操作
             */
            const onSubmit = async () => {
                // 验证所有表单
                const formRefs = [
                    databaseConfigRef.value?.strategyFormRef,
                    databaseConfigRef.value?.configFormRef,
                    ...typeConfigRef.value.map(item => item.formRef)
                ];

                const validations = await Promise.all(formRefs.map(ref => validateForm(ref)));

                // 验证未通过
                if (!validations.every(valid => valid)) {
                    message({message: "请检查输入", type: "error"});
                    return; // 添加return避免继续执行
                }

                const request = {
                    url: "/generator/generate",
                    method: "POST",
                    data: configForm,
                    params: providerFrom,
                    responseType: providerFrom.mode === "download_zip" ? "blob" : undefined
                }

                try {
                    const response = await http(request);

                    if (providerFrom.mode === "download_zip") {
                        downloadZip(response);
                    } else {
                        if (response.code !== 200) {
                            message({message: response.message, type: "error"});
                            return;
                        }
                        generatorViews.value = response.data;
                    }

                    message({message: "生成成功", type: "success"});
                } catch (error) {
                    message({message: "请求失败", type: "error"});
                    console.error("生成失败:", error);
                }
            };

            /**
             * 重置所有表单字段为初始状态
             */
            const resetAllForms = () => {
                resetForm(databaseConfigRef.value?.strategyFormRef);
                resetForm(databaseConfigRef.value?.configFormRef);
                typeConfigRef.value.forEach(item => resetForm(item.formRef))

                message({message: "所有表单已重置", type: "success"});
            };

            /**
             * 加载系统提供的默认配置
             */
            const loadDefaultConfig = async () => {
                try {
                    const response = await http.get("/config/generator-config");

                    if (response.code !== 200) {
                        message({message: response.message, type: "error"});
                        return;
                    }

                    // 获取默认配置
                    const data = response.data;
                    Object.assign(configForm, data);

                    // 判断是否配置了类别
                    const typeMap = data.typeMap;
                    if (!typeMap) {
                        message({message: "类型映射为空", type: "warning"})
                        return;
                    }

                    message({message: "默认配置加载完成", type: "success"});
                } catch (error) {
                    message({message: "加载默认配置失败", type: "error"});
                    console.error("加载配置失败:", error);
                }
            };

            /**
             * 处理标签页切换事件并将当前激活的标签页保存至URL查询参数中
             */
            const onTabChange = (activeName) => {
                const url = new URL(window.location);
                url.searchParams.set('tab', activeName);
                window.history.pushState({}, '', url);
            }

            /**
             * 解析URL查询字符串并恢复上次访问的标签页状态
             */
            const getRouterParams = () => {
                const search = window.location.search;
                const searchParams = parseWithURLSearchParams(search);

                if (!(searchParams && searchParams.tab)) return;
                activeTab.value = searchParams.tab;
            }

            onMounted(() => {
                loadDefaultConfig();
                getRouterParams();
            });

            return {
                databaseConfigRef,
                typeConfigRef,
                activeTab,
                newTableName,
                configForm,
                providerFrom,
                generatorViews,
                copy,
                onSubmit,
                resetAllForms,
                loadDefaultConfig,
                onTabChange,
            };
        },
    });

    // 注册全局组件
    app.component("common-config", CommonConfig);
    app.component("type-config", TypeConfig);
    app.component("template-config", TemplateConfig);
    app.component("config-preview", ConfigPreview);

    app.use(ElementPlus);
    app.mount("#app");
</script>

</html>
