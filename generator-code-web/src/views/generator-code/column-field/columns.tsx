import { NTag } from 'naive-ui';
import type { JSX } from 'vue/jsx-runtime';

export const columns: any = [
  {
    title: '序号',
    key: 'no',
    titleAlign: 'center',
    align: 'center',
    render(row: any, index: number) {
      return index + 1;
    },
  },
  {
    title: '列名称',
    key: 'columnName',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return <NTag type="primary">{row.columnName}</NTag>;
    },
  },
  {
    title: '字段名称',
    key: 'lowercaseName',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return <NTag>{row.lowercaseName}</NTag>;
    },
  },
  {
    title: '数据库字段类型',
    key: 'jdbcType',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return <NTag>{row.jdbcType}</NTag>;
    },
  },
  {
    title: 'Java类型',
    key: 'javaType',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return <NTag type="warning">{row.javaType}</NTag>;
    },
  },
  {
    title: '是否为主键',
    key: 'isPrimaryKey',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return row.isPrimaryKey ? <NTag type="error">是</NTag> : <NTag type="success">否</NTag>;
    },
  },
  {
    title: '字段注释',
    key: 'comment',
    titleAlign: 'center',
    align: 'center',
    render(row: any): JSX.Element {
      return <NTag type="info">{row.comment}</NTag>;
    },
  },
];
