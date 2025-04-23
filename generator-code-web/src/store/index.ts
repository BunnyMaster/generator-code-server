import { createPinia } from 'pinia';
import piniaPluginPersistedState from 'pinia-plugin-persistedstate';
import type { App } from 'vue';

const pinia = createPinia();

// 全局注册 store
export function setupStore(app: App<Element>) {
  pinia.use(piniaPluginPersistedState);
  app.use(pinia);
}
