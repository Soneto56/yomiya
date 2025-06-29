import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [vue({
    template: {
      transformAssetUrls: {
        exclude: ['style'] // 防止LESS文件被错误处理
      }
    }
  })],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    strictPort: true,
    hmr: {
      overlay: false // 禁用错误覆盖层
    },
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  },
  css: {
    devSourcemap: false // 关闭CSS sourcemap
  },
  build: {
    sourcemap: false, // 关闭生产环境sourcemap
    rollupOptions: {
      external: ['web-trans-setting'] // 排除异常模块
    }
  }
})