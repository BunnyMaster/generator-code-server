const TemplateConfig = {
    template: `
<div>
    <el-button-group class="mb-4">
        <el-button @click="onSelectOption('all')">全选</el-button>
        <el-button @click="onSelectOption('invert')">反选</el-button>
        <el-button @click="onSelectOption('none')">全不选</el-button>
    </el-button-group>
    <el-table :data="tableData" ref="templateTableRef" class="w-full md:w-auto" border highlight-current-row row-key="key">
        <!-- 选中 -->
        <el-table-column type="isGenerate" label="选中" width="60" align="center" >
            <template #default="scope">
                <el-checkbox v-model="scope.row.isGenerate" @change="updateTemplate" />
            </template> 
        </el-table-column>
        
        <!-- 序号 -->
        <el-table-column type="index" label="序号" width="60" align="center" />
        
        <!-- 文件扩展名 -->
        <el-table-column prop="fileExtension" label="文件扩展名" width="120">
            <template #default="scope">
                <el-input v-model="scope.row.fileExtension" placeholder="如：.java、.vue" @change="updateTemplate" />
            </template>
        </el-table-column>

        <!-- 文件名类型 -->
        <el-table-column prop="filenameType" label="文件名类型" width="150">
            <template #default="scope">
                <el-select v-model="scope.row.filenameType" placeholder="选择文件名类型" class="w-[100%]"
                    @change="updateTemplate">
                    <el-option v-for="(item,index) in fileTypeList" :key="index" :label="item.label"
                        :value="item.value" />
                </el-select>
            </template>
        </el-table-column>

        <!-- 输出子目录 -->
        <el-table-column prop="outputSubDir" label="输出子目录" width="150">
            <template #default="scope">
                <el-input v-model="scope.row.outputSubDir" placeholder="输出子目录" @change="updateTemplate" />
            </template>
        </el-table-column>

        <!-- 覆盖文件 -->
        <el-table-column prop="overwrite" label="覆盖文件" width="100" align="center">
            <template #default="scope">
                <el-switch v-model="scope.row.overwrite" @change="updateTemplate" />
            </template>
        </el-table-column>

        <!-- 文件前缀 -->
        <el-table-column prop="prefix" label="文件前缀" width="120">
            <template #default="scope">
                <el-input v-model="scope.row.prefix" placeholder="文件前缀" @change="updateTemplate" />
            </template>
        </el-table-column>

        <!-- 文件名后缀 -->
        <el-table-column prop="suffix" label="文件后缀" width="120">
            <template #default="scope">
                <el-input v-model="scope.row.suffix" placeholder="文件后缀" @change="updateTemplate" />
            </template>
        </el-table-column>

        <!-- templatePath -->
        <el-table-column prop="templatePath" label="模板路径" width="200">
            <template #default="scope">
                <el-input v-model="scope.row.templatePath" placeholder="模板文件路径" @change="updateTemplate" />
            </template>
        </el-table-column>

        <el-table-column label="操作" width="80" align="center" fixed="right">
            <template #default="scope">
                <el-button type="danger" link @click="removeTemplate(scope.row.key)">删除</el-button>
            </template>
        </el-table-column>
    </el-table>

    <!-- === 添加新模板 === -->
    <el-card class="mt-4">
        <template #header>
            <div class="flex justify-between items-center">
                <span>添加新模板</span>
            </div>
        </template>

        <el-form :model="newTemplate" ref="templateFormRef" :rules="templateRules" label-width="auto" label-position="left">
            <el-row :gutter="10">
                <!-- key -->
                <el-col :span="8">
                    <el-form-item label="文件key" prop="key">
                        <el-input v-model="newTemplate.key" placeholder="不能重复，否则会替换" />
                    </el-form-item>
                </el-col>
                <!-- 文件扩展名 -->
                <el-col :span="8">
                    <el-form-item label="文件扩展名" prop="fileExtension">
                        <el-input v-model="newTemplate.fileExtension" placeholder="如：.java、.vue" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="10">
                <!-- 文件名类型 -->
                <el-col :span="8">
                    <el-form-item label="文件名类型" prop="filenameType">
                        <el-select v-model="newTemplate.filenameType" placeholder="选择文件名类型" style="width: 100%">
                            <el-option v-for="(item,index) in fileTypeList" :key="index" :label="item.label"
                                :value="item.value" />
                        </el-select>
                    </el-form-item>
                </el-col>

                <!-- 文件前缀 -->
                <el-col :span="8">
                    <el-form-item label="文件前缀" prop="prefix">
                        <el-input v-model="newTemplate.prefix" placeholder="文件前缀" />
                    </el-form-item>
                </el-col>

                <!-- 文件后缀 -->
                <el-col :span="8">
                    <el-form-item label="文件后缀" prop="suffix">
                        <el-input v-model="newTemplate.suffix" placeholder="文件后缀" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="10">
                <!-- 模板路径 -->
                <el-col :span="12">
                    <el-form-item label="模板路径" prop="templatePath">
                        <el-input v-model="newTemplate.templatePath" placeholder="模板文件路径" />
                    </el-form-item>
                </el-col>

                <!-- 输出子目录 -->
                <el-col :span="12">
                    <el-form-item label="输出子目录" prop="outputSubDir">
                        <el-input v-model="newTemplate.outputSubDir" placeholder="输出子目录" />
                    </el-form-item>
                </el-col>
            </el-row>

            <el-row :gutter="10">
                <!-- 覆盖 -->
                <el-col :span="6">
                    <el-form-item label="覆盖" prop="overwrite">
                        <el-checkbox v-model="newTemplate.overwrite">覆盖已存在文件</el-checkbox>
                    </el-form-item>
                </el-col>
                
                <!-- 是否生成 -->
                <el-col :span="6">
                    <el-form-item label="是否生成" prop="isGenerate">
                        <el-checkbox v-model="newTemplate.isGenerate">是否生成</el-checkbox>
                    </el-form-item>
                </el-col>
            </el-row>
                
            <el-form-item>
                <el-button @click="addTemplate" type="primary">添加模板</el-button>
                <el-button @click="resetNewTemplate">重置</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</div>
  `,

    props: {
        templates: {
            type: Object,
        },
    },

    setup(props, {emit}) {
        // 模板表格Ref
        const templateTableRef = ref();
        // 模板表单Ref
        const templateFormRef = ref();

        // 模板表格列表
        const tableData = computed(() => Object.keys(props.templates).map((key) => {
            const template = props.templates[key];
            return {key, ...template,};
        }));

        // 文件类型列表
        const fileTypeList = ref([]);

        const newTemplate = ref({
            key: "",
            fileExtension: ".java",
            filenameType: "UPPER_CAMEL",
            overwrite: true,
            prefix: "",
            suffix: "",
            templatePath: "",
            outputSubDir: "",
            isGenerate: true,
        });

        /**
         * 全选
         */
        const onSelectOption = (type) => {
            let data = tableData.value

            switch (type) {
                // 全选
                case "all":
                    data = tableData.value.reduce((acc, item) => {
                        acc[item.key] = {...item, isGenerate: true};
                        return acc;
                    }, {});
                    break;
                // 反选
                case "invert":
                    data = tableData.value.reduce((acc, item) => {
                        acc[item.key] = {...item, isGenerate: !item.isGenerate};
                        return acc;
                    }, {});
                    break;
                // 全不选
                case "none":
                    data = tableData.value.reduce((acc, item) => {
                        acc[item.key] = {...item, isGenerate: false};
                        return acc;
                    }, {});
                    break;
            }
            emit("update:templates", data);
        }

        /**
         * 获取文件类型枚举列表
         */
        const getFileTypeList = async () => {
            const response = await http.get("/config/filename-type-enums");
            if (response.code !== 200) {
                message({message: response.message, type: "error"});
                return;
            }
            fileTypeList.value = response.data;
        };

        /**
         * 删除模板
         */
        const removeTemplate = async (key) => {
            const result = await openMessageBox({});
            if (!result) return

            const templates = tableData.value.filter((item) => item.key !== key);
            const data = templates.reduce((acc, item) => {
                acc[item.key] = {...item};
                return acc;
            }, {});
            emit("update:templates", data);
        };

        /**
         * 添加模板
         */
        const addTemplate = async () => {
            // 验证所有表单
            const validations = await validateForm(templateFormRef.value);

            // 验证未通过
            if (!validations) {
                message({message: "表单未填写完整", type: "error"});
                return;
            }

            const data = Object.assign({}, props.templates, {
                [newTemplate.value.key]: {...newTemplate.value}
            });
            emit("update:templates", data);
            resetNewTemplate();
        };

        /**
         * 更新模板
         */
        const updateTemplate = () => {
            const data = tableData.value.reduce((acc, item) => {
                acc[item.key] = {...item};
                return acc;
            }, {});
            emit("update:templates", data);
        };

        /**
         * 重置模板
         */
        const resetNewTemplate = () => {
            templateFormRef.value.resetFields();
            message({message: "重置模板完成", type: "success"});
        };

        onMounted(() => {
            getFileTypeList();
        });

        return {
            templateTableRef,
            templateFormRef,
            tableData,
            fileTypeList,
            newTemplate,
            templateRules,
            onSelectOption,
            addTemplate,
            updateTemplate,
            resetNewTemplate,
            removeTemplate,
        };
    },
};