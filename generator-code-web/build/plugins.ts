import UnoCssIcons from '@unocss/preset-icons';
import vue from '@vitejs/plugin-vue';
import vueJsx from '@vitejs/plugin-vue-jsx';
import { presetIcons } from 'unocss';
import UnoCSS from 'unocss/vite';
import type { PluginOption } from 'vite';
import removeConsole from 'vite-plugin-remove-console';
import Inspector from 'vite-plugin-vue-inspector';

import { useCDN } from './cdn';
import { viteConsoleLog } from './info';
import { compressPack, report } from './utils';

export const plugins = (mode: string): PluginOption[] => {
  return [
    vue(),
    vueJsx(),
    Inspector(),
    report(),
    removeConsole(),
    useCDN(mode),
    viteConsoleLog(mode),
    UnoCSS({
      hmrTopLevelAwait: false,
      inspector: true, // 控制台是否打印 UnoCSS inspector
      presets: [
        presetIcons({
          prefix: '',
          extraProperties: {
            display: 'inline-block',
            'vertical-align': 'middle',
          },
        }),
        UnoCssIcons({
          prefix: '',
          extraProperties: {
            display: 'inline-block',
            'vertical-align': 'middle',
          },
        }),
      ],
    }),
    compressPack(mode),
  ];
};
