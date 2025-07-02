<script lang="ts" setup>
import { NDataTable } from 'naive-ui';
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';

import { useTableStore } from '@/store/modules/table';
import { columns } from '@/views/generator-code/column-field/columns';

const route = useRoute();
const tableStore = useTableStore();

// 数据库中当前表的列信息
const datalist = ref([]);

const loading = ref(false);

/* 数据库列信息 */
const getColumnInfo = async () => {
  loading.value = true;

  const tableName: any = route.query.tableName;
  datalist.value = await tableStore.getTableColumnInfo(tableName);

  loading.value = false;
};

onMounted(() => {
  getColumnInfo();
});
</script>

<template>
  <!-- 当前表的列字段 -->
  <n-data-table :bordered="true" :columns="columns" :data="datalist" :loading="loading" />
</template>
