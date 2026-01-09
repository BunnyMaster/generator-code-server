const ConfigPreview = {
    template: `
       <!-- 配置预览 -->
        <el-tabs type="border-card" class="mt-8 mx-3" v-model="activeCollapse">
            <!-- 原配置 -->
            <el-tab-pane label="原配置" name="common">
                <div class="relative">
                    <pre class="whitespace-pre-wrap break-words pr-10">
                        {{ JSON.stringify(form, null, 2) }}
                    </pre>
                    <el-button class="absolute top-2 right-2" size="small" type="primary"
                               @click="copy(JSON.stringify(form, null, 2))">
                        复制
                    </el-button>
                </div>
            </el-tab-pane>

            <!-- 生成策略 -->
            <el-tab-pane label="生成策略" name="strategy">
                <div class="relative">
                    <pre class="whitespace-pre-wrap break-words pr-10">
                        {{ JSON.stringify(generationStrategyFrom, null, 2) }}
                    </pre>
                    <el-button class="absolute top-2 right-2" size="small" type="primary"
                               @click="copy(JSON.stringify(generationStrategyFrom, null, 2))">
                        复制
                    </el-button>
                </div>
            </el-tab-pane>

            <el-tab-pane label="在线展示" name="onlineView">
                <el-collapse accordion>
                    <el-collapse-item v-for="view in generatorViews" :key="view.id" 
                                      :title="view.displayName"
                                      :name="view.id">
                        <el-collapse accordion class="relative" v-for="viewItem in view.children" :key="viewItem.id">
                            <el-collapse-item :title="viewItem.outputDirFile" :name="viewItem.id">
                                <el-input v-model="viewItem.code" class="whitespace-pre-wrap break-words pr-10" autosize
                                          type="textarea"/>
                            </el-collapse-item>
                            <el-button class="absolute top-2 right-2" size="small" type="primary"
                                       @click="copy(JSON.stringify(viewItem.code, null, 2))">
                                复制
                            </el-button>
                        </el-collapse>
                    </el-collapse-item>
                </el-collapse>
            </el-tab-pane> 
        </el-tabs>
`,

    props: {
        form: {type: Object, default: {}},
        generationStrategyFrom: {type: Object, default: {}},
        generatorViews: {type: Array, default: []},
    },

    setup() {
        const activeCollapse = ref('common');

        return {
            activeCollapse,
        };
    },
};
