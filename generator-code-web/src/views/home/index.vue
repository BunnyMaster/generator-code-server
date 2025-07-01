<script lang="tsx" setup>
import { NCard, NDataTable, NInput, NSelect, NTag } from 'naive-ui';
import { storeToRefs } from 'pinia';
import { onMounted, ref } from 'vue';

import { useTableStore } from '@/store/modules/table';
import { columns, renderOptions } from '@/views/home/columns';

const tableStore = useTableStore();
const { tableList, tableListLoading, databaseInfoMeta } = storeToRefs(tableStore);

// 表格数据
const datalist = ref([]);

/* 数据库所有的表 */
const onSearch = async (databaseName: string) => {
  // 加载数据库基础信息，包含当前连接的数据库名称
  await tableStore.loadDatabaseInfoMeta();

  // 如果名称不存在就获取默认的数据库名称
  if (databaseName) {
    databaseInfoMeta.value.currentDatabase = databaseName;
  }

  // 获取完数据库名称开始加载当前数据库所有的表
  await tableStore.loadDatabaseTableList();

  // 将当前的数据库列表放到可以搜索的列表中
  datalist.value = tableList.value;
};

/* 当查询数据表 */
const onChangeQueryText = (val: string) => {
  if (val.trim() == '') {
    datalist.value = tableList.value;
    return;
  }

  datalist.value = datalist.value.filter(({ tableName }) => tableName.includes(val));
};

onMounted(() => {
  onSearch(undefined);
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

    <div class="flex">
      <!-- 选择数据库 -->
      <n-select
        :on-update-value="onSearch"
        :options="databaseInfoMeta?.databaseList"
        :render-option="renderOptions"
        :value="databaseInfoMeta?.currentDatabase"
        class="mt-2 w-[200px]"
        clear-filter-after-select
        clearable
        label-field="tableCat"
        placeholder="选择数据库"
        value-field="tableCat"
      />

      <n-input
        class="mt-2 ml-2"
        placeholder="输入数据表名称"
        style="width: 220px"
        @input="onChangeQueryText"
      />
    </div>
  </n-card>

  <n-data-table
    :bordered="true"
    :columns="columns()"
    :data="datalist"
    :loading="tableListLoading"
  />
</template>
