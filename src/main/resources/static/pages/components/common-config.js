const CommonConfig = {
    template: `
<div>
    <el-form :model="providerFrom" :rules="strategyRules" ref="strategyFormRef" label-width="auto" label-position="left">
        <div class="h3 text-xl font-bold text-teal-400">模式选择</div>
        <el-row :gutter="10">
            <el-col :span="6">
                <el-form-item label="生成的类型" prop="provider">
                    <el-select v-model="providerFrom.provider" placeholder="选择要生成的类型">
                        <el-option v-for="item in supportMap.supportProvider" :key="item.value" :label="item.label"
                            :value="item.value" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="6">
                <el-form-item label="生成模式" prop="mode">
                    <el-select v-model="providerFrom.mode" placeholder="选择要生成的模式">
                      <el-option v-for="item in supportMap.supportModes" :key="item.value" :label="item.label" :value="item.value" />
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>
    </el-form>

    <el-form :model="form" :rules="commonConfigRules" ref="configFormRef" label-width="auto" label-position="left">
        <div class="h3 text-xl font-bold text-teal-400">基础信息</div>
        <el-row :gutter="10">
            <el-col :span="8">
                <el-form-item label="作者" prop="author">
                    <el-input v-model="form.author" placeholder="请输入作者名称" clearable />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="时间格式" prop="commentTimeFormat">
                    <el-input v-model="form.generatorTimeFormatter" clearable placeholder="请输入时间格式" />
                </el-form-item>
            </el-col>
            <el-col :span="8">
                <el-form-item label="基础输出目录" prop="baseOutputDir">
                    <el-input v-model="form.baseOutputDir" clearable placeholder="请输入输出目录路径" />
                </el-form-item>
            </el-col>
        </el-row>

        <div class="h3 text-xl font-bold text-teal-400">表配置</div>
        <el-form-item label="忽略表前缀">
            <div class="dynamic-tags">
                <el-tag v-for="(prefix, index) in form.ignoreTablePrefixes" :key="index" closable class="mr-2 mb-2"
                    @close="removeTablePrefixes(index)" round>
                    {{ prefix }}
                </el-tag>
            </div>
            <el-input v-model="newIgnorePrefix" class="tag-input" placeholder="请输入表前缀" clearable @keyup.enter="addIgnorePrefix">
                <template #append>
                    <el-button @click="addIgnorePrefix">添加</el-button>
                </template>
            </el-input>
        </el-form-item>
        
        <!-- 其他额外配置 -->
        <div class="h3 text-xl font-bold text-teal-400">其他额外配置</div>
        <slot/>
    </el-form>
</div>
  `,
    props: {
        form: Object,
        providerFrom: Object,
    },
    emits: ['update:form', 'update:practicalForms'],
    setup(props) {
        const strategyFormRef = ref();
        const configFormRef = ref();
        const newIgnorePrefix = ref('');

        // 支持哪些生成内容
        const supportMap = reactive({
            supportProvider: [],
            supportModes: [],
        });

        /**
         * 获取支持哪些生成内容
         * @returns supportMap
         */
        const getSupportMap = async () => {
            const response = await http.get('/config/support');
            if (response.code !== 200) {
                message({message: response.message, type: 'error'});
                return;
            }

            Object.assign(supportMap, response.data);
        };

        /**
         * 添加忽略前缀
         */
        const addIgnorePrefix = () => {
            if (newIgnorePrefix.value && !props.form.ignoreTablePrefixes.includes(newIgnorePrefix.value)) {
                props.form.ignoreTablePrefixes.push(newIgnorePrefix.value);
                newIgnorePrefix.value = '';
                return;
            }

            message({message: '前缀未填写或忽略前缀已经存在', type: 'warning'});
        };

        /**
         * 移除忽略前缀
         * @param index 索引
         */
        const removeTablePrefixes = (index) => {
            props.form.ignoreTablePrefixes.splice(index, 1);
        };

        onMounted(() => {
            getSupportMap();
        });

        return {
            strategyFormRef,
            configFormRef,
            newIgnorePrefix,
            supportMap,
            addIgnorePrefix,
            removeTablePrefixes,
            commonConfigRules,
            strategyRules,
        };
    },
};
