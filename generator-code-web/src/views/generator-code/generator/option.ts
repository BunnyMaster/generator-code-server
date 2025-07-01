import type { FormRules } from 'naive-ui';
import { storeToRefs } from 'pinia';

import { useVmsStore } from '@/store/modules/vms';

const vmsStore = useVmsStore();
const { formOption } = storeToRefs(vmsStore);

/* 验证生成前后端内容是否合法 */
const validatorFormOption = () => {
  return formOption.value.generatorServer.length > 0 || formOption.value.generatorWeb.length > 0;
};

// 表单验证
export const rules: FormRules = {
  author: { required: true, trigger: ['blur', 'change', 'input'], message: '作者不能为空' },
  packageName: { required: true, trigger: ['blur', 'change', 'input'], message: '包名不能为空' },
  requestMapping: {
    required: true,
    trigger: ['blur', 'change', 'input'],
    message: '请求路径不能为空',
  },
  className: { required: true, trigger: ['blur', 'change', 'input'], message: '类名不能为空' },
  tableName: { required: true, trigger: ['blur', 'change', 'input'], message: '表名不能为空' },
  simpleDateFormat: {
    required: true,
    trigger: ['blur', 'change', 'input'],
    message: '日期格式不能为空',
  },
  generatorServer: [
    {
      required: true,
      trigger: ['blur', 'change', 'input'],
      message: '选择要生成的服务端',
      validator: validatorFormOption,
    },
  ],
  generatorWeb: [
    {
      required: true,
      trigger: ['blur', 'change', 'input'],
      message: '选择要生成的前端',
      validator: validatorFormOption,
    },
  ],
};
