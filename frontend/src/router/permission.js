// src/router/permission.js
import router from './index'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const whiteList = ['/login', '/register'] // 免登录白名单

router.beforeEach(async (to, from, next) => {
  const userStore = useUserStore()

  // 1. 检查是否需要登录
  if (to.meta.requiresAuth) {
    // 2. 检查token是否存在
    if (userStore.token) {
      // 3. 如果用户信息未加载，先获取用户信息
      if (!userStore.userInfo) {
        try {
          await userStore.fetchProfile()
          // 4. 动态添加权限路由（如需）
          // await userStore.generateRoutes()
        } catch (error) {
          // token失效处理
          userStore.logout()
          ElMessage.error('登录已过期，请重新登录')
          return next(`/login?redirect=${to.path}`)
        }
      }
      // 5. 检查角色权限
      if (to.meta.roles && !to.meta.roles.includes(userStore.role)) {
        return next('/403') // 无权限页面
      }
      next()
    } else {
      // 未登录且需要认证的路由
      next(`/login?redirect=${to.path}`)
    }
  } else if (to.meta.guestOnly && userStore.token) {
    // 已登录用户禁止访问guest页面（如登录页）
    next('/')
  } else {
    // 放行非权限路由
    next()
  }
})