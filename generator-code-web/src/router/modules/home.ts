import type { RouteRecordRaw } from 'vue-router';

import Layout from '@/layout/index.vue';
import type { RouteConfigsTable } from '@/types/router/Route';

const routes: RouteRecordRaw[] | RouteConfigsTable[] = [
  {
    path: '/',
    name: '/',
    component: Layout,
    redirect: '/home',
    meta: { transition: 'fade' },
    children: [
      { path: '/home', name: 'home', component: () => import('@/views/home/index.vue') },
      {
        path: '/generator-code',
        name: 'generatorCode',
        component: () => import('@/views/generator-code/index.vue'),
      },
    ],
  },
];

export default routes;
