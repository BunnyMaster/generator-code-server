<script lang="ts" setup>
import { NGradientText } from 'naive-ui';
import { storeToRefs } from 'pinia';
import { onMounted } from 'vue';

import { useTableStore } from '@/store/modules/table';

const tableStore = useTableStore();
const { databaseInfoMeta } = storeToRefs(tableStore);

onMounted(() => {
  tableStore.loadDatabaseInfoMeta();
});
</script>

<template>
  <div class="database-info">
    <p>
      数据库产品名称：
      <n-gradient-text type="primary">{{ databaseInfoMeta?.databaseProductName }}</n-gradient-text>
    </p>
    <p>
      数据库产品版本：
      <n-gradient-text type="primary">
        {{ databaseInfoMeta?.databaseProductVersion }}
      </n-gradient-text>
    </p>
    <p>
      驱动名称：
      <n-gradient-text type="primary">{{ databaseInfoMeta?.driverName }}</n-gradient-text>
    </p>
    <p>
      数据库驱动版本：
      <n-gradient-text type="primary">{{ databaseInfoMeta?.driverVersion }}</n-gradient-text>
    </p>
    <p>
      数据库用户：
      <n-gradient-text type="primary">{{ databaseInfoMeta?.username }}</n-gradient-text>
    </p>
  </div>
</template>

<style lang="scss" scoped>
.database-info {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  flex-direction: column;

  p {
    margin: 5px 0 0 0;
  }
}
</style>
