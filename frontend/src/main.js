import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 解决sessionStorage错误
if (!window.sessionStorage) {
  Object.defineProperty(window, 'sessionStorage', {
    value: {
      getItem: () => null,
      setItem: () => {}
    },
    writable: false
  })
}

const app = createApp(App)

app.config.errorHandler = (err) => {
  console.error('Global error:', err)
}

app.use(createPinia())
app.use(router)
app.use(ElementPlus)

router.isReady().then(() => {
  app.mount('#app')
}).catch(err => {
  console.error('Router initialization failed:', err)
})