import request from '@/api/server/request';
import type { BaseResult } from '@/types/request'; /* 获取所有数据表 */

/* 获取所有数据表 */
export const generator = (data: any) => {
  return request<any, BaseResult<any>>({ url: '/vms/generator', method: 'post', data });
};

/* 获取vms文件路径 */
export const getVmsResourcePathList = () => {
  return request<any, BaseResult<any>>({ url: '/vms/vmsResourcePathList', method: 'get' });
};

/* 打包成zip下载 */
export const downloadByZip = (data: any) => {
  return request<any, any>({
    url: '/vms/downloadByZip',
    method: 'POST',
    data,
    responseType: 'blob',
  });
};
