<script lang="tsx" setup>
import { NCard, NDataTable, NSelect, NTag } from 'naive-ui';
import { storeToRefs } from 'pinia';
import { onMounted } from 'vue';

import { getCurrentDatabaseName } from '@/api/table';
import { useTableStore } from '@/store/modules/table';
import { columns, renderOptions } from '@/views/home/columns';

const tableStore = useTableStore();
const { tableList, dbList, tableListLoading, currentDatabaseName } = storeToRefs(tableStore);

/* 更新数据库名称 */
const onUpdateCurrentDatabaseName = (databaseName: string) => {
  tableStore.currentDatabaseName = databaseName ?? undefined;
  tableStore.getDatabaseTableList();
};

/* 数据库所有的表 */
const initDatabaseTables = async () => {
  const result = await getCurrentDatabaseName();
  if (result.code === 200) {
    tableStore.currentDatabaseName = result.data;
  }
  await tableStore.getDatabaseTableList();
};

onMounted(() => {
  initDatabaseTables();
  tableStore.getDatabaseList();
});
</script>

<template>
  <n-card class="my-2" title="提示">
    <p>
      点击
      <n-tag>表名</n-tag>
      或
      <n-tag>注释内容</n-tag>
      跳转
    </p>
    <p class="mt-2">
      数据库共
      <n-tag type="info">{{ tableList.length }}</n-tag>
      张表
    </p>

    <!-- 选择数据库 -->
    <n-select
      :on-update-value="onUpdateCurrentDatabaseName"
      :options="dbList"
      :render-option="renderOptions"
      :value="currentDatabaseName"
      class="mt-2 w-[200px]"
      clear-filter-after-select
      clearable
      placeholder="选择数据库"
    />
  </n-card>

  <n-data-table
    :bordered="true"
    :columns="columns()"
    :data="tableList"
    :loading="tableListLoading"
  />
</template>
