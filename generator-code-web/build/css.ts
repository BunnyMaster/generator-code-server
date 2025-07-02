import type { CSSOptions } from 'vite';


export const css = (mode: string): CSSOptions => {
  return {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/assets/styles/minix/sidebar" as  *;`,
      },
    },
  };
};
