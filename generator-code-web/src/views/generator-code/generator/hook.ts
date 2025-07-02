import { storeToRefs } from 'pinia';

import { useVmsStore } from '@/store/modules/vms';

const vmsStore = useVmsStore();
const { formValue, formOption } = storeToRefs(vmsStore);

/* 初始化表单信息 */
export const formValueInit = (tableName: any) => {
  formValue.value.tableName = tableName.toString();

  // 替换类名称
  let className: any = tableName as any;
  formValue.value.tablePrefixes.split(/[,，]/).forEach((item) => {
    className = className.replace(item, '');
  });
  formValue.value.className = className;
};

/* 全部选择 */
export const selectAll = () => {
  formOption.value.generatorServer = vmsStore.serverOptions.map((option: any) => option.name);
  formOption.value.generatorWeb = vmsStore.webOptions.map((option: any) => option.name);
};

/* 全部反选 */
export const selectAllInvert = () => {
  // 反选server
  const serverNames: string[] = vmsStore.serverOptions.map((item) => item['name']);
  const generatorServer = formOption.value.generatorServer;
  formOption.value.generatorServer = serverNames.filter(
    (name: string) => !generatorServer.includes(name)
  );

  // 反选web
  const webNames: string[] = vmsStore.webOptions.map((item) => item['name']);
  const generatorWeb = formOption.value.generatorWeb;
  formOption.value.generatorWeb = webNames.filter((name) => !generatorWeb.includes(name));
};

/* 取消全部选择 */
export const selectCancelAll = () => {
  formOption.value.generatorServer = [];
  formOption.value.generatorWeb = [];
  formValue.value.path = [];
};

/* 验证 formValue.path 是否为空  */
export const validateFormValue = () => {
  // 选择要生成的模板
  const web = formOption.value.generatorWeb;
  const server = formOption.value.generatorServer;

  // 整理好数据
  formValue.value.path = [...server, ...web];
  if (formValue.value.path.length <= 0) {
    (window as any).$message.error(`选择要生成的模板`);
    return;
  }
};
