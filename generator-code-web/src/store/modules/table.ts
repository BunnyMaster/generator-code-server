import { defineStore } from 'pinia';

import {
  getDatabaseInfoMetaData,
  getDatabaseList,
  getDatabaseTableList,
  getTableColumnInfo,
  getTableMetaData,
} from '@/api/table';

export const useTableStore = defineStore('tableStore', {
  state: () => ({
    // 当前的数据库名称
    currentDatabaseName: undefined,
    databaseInfoMeta: undefined,
    // 数据库所有的表
    tableList: [],
    tableListLoading: false,
    // 数据列表
    dbList: [],
  }),
  getters: {},
  actions: {
    /* 当前数据库信息 */
    async getDatabaseInfoMeta() {
      const result = await getDatabaseInfoMetaData();
      if (result.code === 200) {
        this.databaseInfoMeta = result.data;
      }
    },

    /* 所有的数据库 */
    async getDatabaseList() {
      this.tableListLoading = true;
      const result = await getDatabaseList();

      if (result.code !== 200) {
        this.tableListLoading = false;
        return;
      }

      // 整理返回数据格式
      const list = result.data.map((db: any) => ({
        label: db.tableCat,
        value: db.tableCat,
        comment: db.comment,
      }));

      // 在开头添加
      list.unshift({ label: '无', value: undefined, comment: '查询全部' });

      this.dbList = list;
      this.tableListLoading = false;
    },

    /* 数据库所有的表 */
    async getDatabaseTableList() {
      const data = { dbName: this.currentDatabaseName };
      const result = await getDatabaseTableList(data);

      this.tableList = result.data;
    },

    /* 获取表属性 */
    async getTableMetaData(tableName: string) {
      const result = await getTableMetaData({ tableName });
      if (result.code !== 200) {
        return {};
      }

      return result.data;
    },

    /* 获取表属性 */
    async getTableColumnInfo(tableName: string) {
      const result = await getTableColumnInfo({ tableName });
      if (result.code !== 200) {
        return {};
      }

      return result.data;
    },
  },
});
