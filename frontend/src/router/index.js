// index.js
import { createRouter, createWebHistory } from 'vue-router'
import routes from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 }
  }
})

// 添加路由钩子，用于调试
router.beforeEach((to, from, next) => {
  console.log(`即将导航到: ${to.path}`)
  next()
})

export default router