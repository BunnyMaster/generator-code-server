<script lang="ts" setup>
import { NButton, NButtonGroup } from 'naive-ui';

const props = defineProps({
  // 数据源（必须包含唯一标识字段，默认是 `id`）
  data: {
    type: Array,
    required: true,
  },
  // 当前选中的数据
  selected: {
    type: Array,
    default: () => [],
  },
  // 唯一标识字段名（默认 `id`）
  idKey: {
    type: String,
    default: 'name',
  },
});
const emit = defineEmits(['update:selected']);

/* 全选 */
const selectAll = () => {
  const allNames = props.data.map((item) => item[props.idKey]);
  emit('update:selected', [...allNames]);
};

/* 反选 */
const selectInvert = () => {
  const currentSelected = props.selected;
  const allNames = props.data.map((item) => item[props.idKey]);
  const inverted = allNames.filter((name) => !currentSelected.includes(name));
  emit('update:selected', inverted);
};

/* 全不选 */
const selectCancel = () => {
  emit('update:selected', []);
};
</script>

<template>
  <n-button-group size="small">
    <n-button round type="primary" @click="selectAll">全选</n-button>
    <n-button type="warning" @click="selectInvert">反选</n-button>
    <n-button round type="error" @click="selectCancel">全不选</n-button>
  </n-button-group>
</template>
