<script lang="ts" setup>
import {
  NButton,
  NButtonGroup,
  NCheckbox,
  NCheckboxGroup,
  NForm,
  NFormItemGi,
  NGrid,
  NGridItem,
  NInput,
  NSpace,
  useMessage,
} from 'naive-ui';
import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';
import { computed } from 'vue-demi';
import { useRoute } from 'vue-router';

import { fetchTableInfo } from '@/api/sqlParser';
import { downloadByZip } from '@/api/vms';
import SelectButtonGroup from '@/components/select-button-group.vue';
import { useVmsStore } from '@/store/modules/vms';
import { downloadBlob, downloadTextAsFile } from '@/utils/file';
import {
  formValueInit,
  selectAll,
  selectAllInvert,
  selectCancelAll,
  validateFormValue,
} from '@/views/generator-code/generator/hook';
import { rules } from '@/views/generator-code/generator/option';

const route = useRoute();

const vmsStore = useVmsStore();
const { formValue, formOption } = storeToRefs(vmsStore);

const message = useMessage();
const formRef = ref();
const hasDownloadZip = computed(
  () => !(formOption.value.generatorWeb.length > 0 || formOption.value.generatorServer.length > 0)
);

// 解析 Sql 语句
const sql = ref();

/* 提交表单 */
const onSubmit = (e: MouseEvent) => {
  e.preventDefault();

  formRef.value?.validate(async (errors: any) => {
    if (!errors) {
      validateFormValue();

      // 生成代码
      await vmsStore.generator(formValue.value);
    } else {
      errors.forEach((error: any) => {
        error.forEach((err: any) => {
          message.error(`${err.message}->${err.field}`);
        });
      });
    }
  });
};

/* 清空已经生成的代码 */
const clearGeneratorCode = () => {
  vmsStore.generators = [];
};

/* 下载全部 */
const downloadAll = () => {
  vmsStore.generators.forEach((vms) => {
    const code = vms.code;
    const path = vms.path.split('/')[1];

    downloadTextAsFile(code, path);
  });
};

/* 下载zip文件 */
const downloadZipFile = async () => {
  validateFormValue();

  const result = await downloadByZip(formValue.value);
  downloadBlob(result);
};

/* 解析 SQL 语句信息 */
const sqlParser = async () => {
  if (!sql.value) {
    message.warning('SQL 为空');
    return;
  }

  const params = { sql: sql.value };
  const { data } = await fetchTableInfo(params);

  // 设置要生成的sql表中的内容
  formValue.value.comment = data.comment;
  formValue.value.tableName = data.tableName;
  formValueInit(data.tableName);
};

onMounted(() => {
  // 初始化表名称
  const tableName: any = route.query.tableName;
  formValueInit(tableName);

  vmsStore.getVmsResourcePathList();
});
</script>

<template>
  <n-form ref="formRef" :label-width="80" :model="formValue" :rules="rules">
    <n-grid cols="24" item-responsive responsive="screen">
      <n-form-item-gi
        label="如果有sql会生成sql中的信息，点击【解析SQL】会替换【表名称】和【注释名称】"
        path="sql"
        span="24"
      >
        <div class="flex flex-wrap flex-col w-full">
          <n-input
            v-model:value="sql"
            :autosize="{ minRows: 3 }"
            class="w-full"
            placeholder="SQL语句"
            type="textarea"
          />

          <n-button-group class="mt-2">
            <n-button type="primary" @click="sqlParser">解析SQL</n-button>
            <n-button type="error" @click="sql = null">清空输入框</n-button>
          </n-button-group>
        </div>
      </n-form-item-gi>
    </n-grid>

    <!-- 需要提交的生成表单 -->
    <n-grid :cols="24" :x-gap="5" item-responsive responsive="screen">
      <n-form-item-gi label="作者名称" path="author" span="12 m:8 l:6">
        <n-input v-model:value="formValue.author" placeholder="作者名称" />
      </n-form-item-gi>

      <n-form-item-gi label="requestMapping名称" path="requestMapping" span="12 m:8 l:6">
        <n-input v-model:value="formValue.requestMapping" placeholder="requestMapping名称" />
      </n-form-item-gi>

      <n-form-item-gi label="表名称" path="tableName" span="24 m:8 l:6">
        <n-input v-model:value="formValue.tableName" placeholder="表名称" />
      </n-form-item-gi>

      <n-form-item-gi label="类名称" path="className" span="24 m:8 l:6">
        <n-input v-model:value="formValue.className" placeholder="类名称" />
      </n-form-item-gi>

      <n-form-item-gi label="包名称" path="packageName" span="24 m:8 l:6">
        <n-input v-model:value="formValue.packageName" placeholder="包名称" />
      </n-form-item-gi>

      <n-form-item-gi label="时间格式" path="simpleDateFormat" span="24 m:8 l:6">
        <n-input v-model:value="formValue.simpleDateFormat" placeholder="时间格式" />
      </n-form-item-gi>

      <n-form-item-gi label="去除开头前缀" path="tablePrefixes" span="12 m:8 l:6">
        <n-input v-model:value="formValue.tablePrefixes" placeholder="去除开头前缀" />
      </n-form-item-gi>

      <n-form-item-gi label="注释名称" path="comment" span="12 m:8 l:6">
        <n-input v-model:value="formValue.comment" placeholder="修改注释名称" />
      </n-form-item-gi>
    </n-grid>

    <!-- 需要生成的模板 -->
    <n-grid :cols="24" :x-gap="5" item-responsive responsive="screen">
      <n-form-item-gi label="生成后端" path="generatorServer" span="24 m:24 l:12">
        <n-checkbox-group v-model:value="formOption.generatorServer">
          <n-space>
            <n-checkbox
              v-for="(item, index) in vmsStore.serverOptions"
              :key="index"
              :value="item.name"
            >
              {{ item.label }}
            </n-checkbox>

            <!-- 选择按钮 -->
            <select-button-group
              v-model:selected="formOption.generatorServer"
              :data="vmsStore.serverOptions"
              id-key="name"
            />
          </n-space>
        </n-checkbox-group>
      </n-form-item-gi>

      <n-form-item-gi label="生成前端" path="generatorWeb" span="24 m:24 l:12">
        <n-checkbox-group v-model:value="formOption.generatorWeb">
          <n-space>
            <n-checkbox
              v-for="(item, index) in vmsStore.webOptions"
              :key="index"
              v-model:value="item.name"
            >
              {{ item.label }}
            </n-checkbox>

            <!-- 选择按钮 -->
            <select-button-group
              v-model:selected="formOption.generatorWeb"
              :data="vmsStore.webOptions"
              id-key="name"
            />
          </n-space>
        </n-checkbox-group>
      </n-form-item-gi>
    </n-grid>

    <!-- 操作选项按钮 -->
    <n-grid cols="24" item-responsive responsive="screen">
      <n-grid-item class="mt-2" span="24 m:12 l:8">
        <n-button attr-type="button" type="success" @click="selectAll">全部选择</n-button>
        <n-button attr-type="button" type="warning" @click="selectAllInvert">全部反选</n-button>
        <n-button attr-type="button" type="error" @click="selectCancelAll">全选取消</n-button>
      </n-grid-item>

      <n-grid-item class="mt-2" span="24 m:12 l:8">
        <n-button attr-type="button" type="success" @click="onSubmit">开始生成</n-button>
        <n-button attr-type="button" type="error" @click="clearGeneratorCode">清空已生成</n-button>
        <n-button
          :disabled="!(vmsStore.generators.length > 0)"
          attr-type="button"
          type="primary"
          @click="downloadAll"
        >
          下载全部 {{ vmsStore.generators.length }}
        </n-button>
      </n-grid-item>

      <n-grid-item class="mt-2" span="24 m:12 l:8">
        <n-button
          :disabled="hasDownloadZip"
          attr-type="button"
          class="w-full"
          type="success"
          @click="downloadZipFile"
        >
          下载zip
        </n-button>
      </n-grid-item>
    </n-grid>
  </n-form>
</template>
