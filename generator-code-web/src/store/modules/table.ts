import { defineStore } from 'pinia';

import {
  getDatabaseInfoMetaData,
  getDatabaseTableList,
  getTableColumnInfo,
  getTableMetaData,
} from '@/api/table';

export const useTableStore = defineStore('tableStore', {
  state: () => ({
    databaseInfoMeta: undefined,
    // 数据库所有的表
    tableList: [],
    tableListLoading: false,
  }),
  getters: {},
  actions: {
    /* 当前数据库信息 */
    async loadDatabaseInfoMeta() {
      const result = await getDatabaseInfoMetaData();
      if (result.code === 200) {
        this.databaseInfoMeta = result.data;
      }
    },

    /* 数据库所有的表 */
    async loadDatabaseTableList() {
      this.tableListLoading = true;
      const dbName = this.databaseInfoMeta?.currentDatabase;
      const result = await getDatabaseTableList({ dbName });

      this.tableList = result.data;
      this.tableListLoading = false;
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
