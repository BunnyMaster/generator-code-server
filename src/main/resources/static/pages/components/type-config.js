// 服务端配置组件
const TypeConfig = {
    template: `
    <el-form ref="formRef" :model="localForm" :rules="typeConfigRules" v-if="localForm" label-width="auto" label-position="left">
      <div class="h3 text-xl font-bold text-teal-400">基础配置</div>
      
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="显示名称" prop="displayName">
            <el-input v-model="localForm.displayName" placeholder="在tab栏上显示的名称" disabled/>
          </el-form-item>
        </el-col>        
        
        <el-col :span="12">
          <el-form-item label="请求映射前缀" prop="requestPrefix">
            <el-input v-model="localForm.requestPrefix" placeholder="/api" />
          </el-form-item>
        </el-col>
        
        <el-col :span="12">
          <el-form-item label="包名称" prop="packagePath">
            <el-input v-model="localForm.packagePath" placeholder="com.example、/views/user" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="输出根目录" prop="typeOutputDir">
            <el-input v-model="localForm.typeOutputDir" placeholder="/src/main/java" />
          </el-form-item>
        </el-col>
      </el-row>

      <!-- 模板配置 -->
      <div class="h3 text-xl font-bold text-teal-400">模板配置</div>
      <template-config ref="templateConfigRef" v-model:templates="localForm.templates"/>
    </el-form>
  `,
    props: {
        form: Object,
        typeMapKey: String,
    },
    emits: ['update:form'],
    setup(props, {emit}) {
        const formRef = ref();
        const templateConfigRef = ref();

        const localForm = reactive({...props.form});

        const setTypeConfigForm = () => {
            Object.assign(localForm, props.form);

            watch(() => localForm, (newValue) => {
                emit("update:form", newValue);
            }, {deep: true})
        }

        onMounted(() => {
            nextTick(() => {
                setTypeConfigForm();
            });
        });

        return {
            formRef,
            templateConfigRef,
            localForm,
            typeConfigRules,
        };
    }
};