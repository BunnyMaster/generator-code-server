import 'highlight.js/styles/atom-one-dark.css';
import 'highlight.js/lib/common';

import highlightJSPlugin from '@highlightjs/vue-plugin';
import type { App } from 'vue';

export const setupHighLight = (app: App<Element>) => {
  app.use(highlightJSPlugin);
};
