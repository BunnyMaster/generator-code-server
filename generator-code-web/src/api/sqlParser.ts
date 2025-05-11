import request from '@/api/server/request';
import type { BaseResult } from '@/types/request';

/* 当前数据库信息 */
export const fetchTableInfo = (params: any) => {
  return request<any, BaseResult<any>>({
    url: '/sqlParser/tableInfo',
    method: 'POST',
    params,
  });
};

/* 当前数据库信息 */
export const fetchColumnMetaData = (params: any) => {
  return request<any, BaseResult<any>>({
    url: '/sqlParser/columnMetaData',
    method: 'POST',
    params,
  });
};
