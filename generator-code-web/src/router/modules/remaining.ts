import type { RouteRecordRaw } from 'vue-router';

import Layout from '@/layout/index.vue';
import type { RouteConfigsTable } from '@/types/router/Route';

const routes: RouteRecordRaw[] | RouteConfigsTable[] = [
  {
    path: '/redirect',
    component: Layout,
    meta: { hidden: true },
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect/index.vue'),
      },
    ],
  },
];

export default routes;
