import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import locale from 'element-plus/dist/locale/zh-cn.mjs'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import 'element-plus/dist/index.css'
import '@/styles/index.scss' // 全局样式

// 初始化权限控制
//import './permission'

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

// 全局错误处理
app.config.errorHandler = (err, vm, info) => {
  console.error('Global error:', err, info)
  // 可以在这里集成错误上报服务
}

// 全局属性
app.config.globalProperties.$filters = {
  formatDate(value) {
    // 全局日期格式化过滤器
    return value // 实际实现日期格式化
  }
}

// 注册Element Plus图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 使用插件
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale })

// 全局指令
app.directive('focus', {
  mounted(el) {
    el.focus()
  }
})

// 等待路由准备就绪再挂载应用
router.isReady()
  .then(() => {
    app.mount('#app')

    // 开发环境日志
    if (import.meta.env.DEV) {
      console.log('App mounted')
    }
  })
  .catch(err => {
    console.error('Router initialization failed:', err)
    // 可以在这里显示友好的错误页面
    document.getElementById('app').innerHTML = `
      <div style="text-align: center; padding: 50px;">
        <h2>系统初始化失败</h2>
        <p>${err.message}</p>
        <p>请刷新页面或联系管理员</p>
      </div>
    `
  })

// 开发环境暴露app
if (import.meta.env.DEV) {
  window.__APP__ = app
}