import type { RouteRecordRaw } from 'vue-router';

import type { RouteConfigsTable } from '@/types/router/Route';

const routes: RouteRecordRaw[] | RouteConfigsTable[] = [
  {
    path: '/error',
    component: () => import('@/views/error-page/404.vue'),
    meta: { hidden: true },
  },
];

export default routes;
