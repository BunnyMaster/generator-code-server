<script lang="tsx" setup>
import hljsVuePlugin from '@highlightjs/vue-plugin';
import { CopySharp } from '@vicons/ionicons5';
import { NButton, NCollapse, NCollapseItem, NIcon, NInput, useDialog, useMessage } from 'naive-ui';
import { ref } from 'vue';

import { useVmsStore } from '@/store/modules/vms';
import { copy } from '@/utils/copy';
import { downloadTextAsFile } from '@/utils/file';

// 使用高亮组件
const HighlightJS = hljsVuePlugin.component;

const dialog = useDialog();
const message = useMessage();
const vmsStore = useVmsStore();

/* 下载文件 */
const download = (code: string, filename: string) => {
  const filenameSplit = filename.split('/');

  // 拿到最后一个元素
  const inputValue = ref(filenameSplit.at(-1));

  dialog.info({
    title: '修改文件名',
    positiveText: '下载',
    negativeText: '取消',
    content: () => (
      <NInput
        placeholder="Tiny Input"
        clearable
        value={inputValue.value}
        onInput={(value: any) => (inputValue.value = value)}
      />
    ),
    onPositiveClick: () => {
      // 下载文件
      downloadTextAsFile(code, inputValue.value);
    },
    onNegativeClick: () => {
      message.info('取消下载');
    },
  });
};
</script>

<template>
  <n-collapse v-show="vmsStore.generators.length > 0" class="mt-4 p-2 border">
    <n-collapse-item
      v-for="(item, index) in vmsStore.generators"
      :key="index"
      :name="item.path"
      :title="item.path"
      class="pos-relative"
    >
      <template #header-extra>
        <n-button quaternary type="info" @click="download(item.code, item.path)">下载</n-button>
      </template>

      <n-button class="pos-absolute right-0" color="#ff69b4" quaternary @click="copy(item.code)">
        <template #icon>
          <n-icon>
            <CopySharp />
          </n-icon>
        </template>
        复制
      </n-button>
      <HighlightJS :autodetect="true" :code="item.code" language="JavaScript" />
    </n-collapse-item>
  </n-collapse>
</template>
