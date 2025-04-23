import { defineStore } from 'pinia';

import { generator, getVmsResourcePathList } from '@/api/vms';

export const useVmsStore = defineStore('vmsStore', {
  // 开启持久化
  // persist: true,
  // persist: {
  //   paths: ['formValue', 'formOption'],
  // },
  state: () => ({
    generators: [],

    // 生成服务端内容
    serverOptions: [],

    // 生成前端内容
    webOptions: [],

    // 查询的表单
    formValue: {
      author: 'Bunny',
      packageName: 'cn.bunny.services',
      requestMapping: '/api',
      className: '',
      tableName: '',
      simpleDateFormat: 'yyyy-MM-dd HH:mm:ss',
      tablePrefixes: 't_,sys_,qrtz_,log_',
      comment: '',
      path: [],
    },

    // 表单选择内容
    formOption: {
      generatorServer: [],
      generatorWeb: [],
    },
  }),
  getters: {},
  actions: {
    /* 获取所有数据表 */
    async generator(data: any) {
      const result = await generator(data);
      // 需要确保已经在 setup 中执行了 window.$message = message

      this.generators = result.data.map((i: any) => ({ ...i, path: i.path.replace('.vm', '') }));
      (window as any).$message.success(`生成成功，共 ${this.generators.length} 数据`);
    },

    /* 获取vms文件路径 */
    async getVmsResourcePathList() {
      const result = await getVmsResourcePathList();
      // 需要确保已经在 setup 中执行了 window.$message = message
      if (result.code !== 200) {
        return {};
      }

      this.webOptions = result.data.web;
      this.serverOptions = result.data.server;
    },
  },
});
