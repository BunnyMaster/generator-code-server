import request from '@/api/server/request';
import type { BaseResult } from '@/types/request';

/* 当前数据库信息 */
export const getDatabaseInfoMetaData = () => {
  return request<any, BaseResult<any>>({ url: '/table/databaseInfoMetaData', method: 'GET' });
};

/* 数据库所有的表 */
export const getDatabaseTableList = (params: any) => {
  return request<any, BaseResult<any>>({ url: '/table/databaseTableList', method: 'get', params });
};

/* 获取表属性 */
export const getTableMetaData = (params: object) => {
  return request<any, BaseResult<any>>({ url: '/table/tableMetaData', method: 'get', params });
};

/* 获取列属性 */
export const getTableColumnInfo = (params: object) => {
  return request<any, BaseResult<any>>({ url: '/table/tableColumnInfo', method: 'get', params });
};
