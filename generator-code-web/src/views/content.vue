<template>&nbsp;</template>

<script>
import { useMessage } from 'naive-ui';
import { defineComponent } from 'vue';

// naive ui 要求这样写的 https://www.naiveui.com/zh-CN/light/components/message
export default defineComponent({
  setup() {
    window.$message = useMessage();
  },
});
</script>
