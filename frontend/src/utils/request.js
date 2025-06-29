import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 10000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers.Authorization = `Bearer ${userStore.token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    // 可以在这里处理通用的成功消息
    if (response.data?.message) {
      ElMessage.success(response.data.message)
    }
    return response.data
  },
  error => {
    const userStore = useUserStore()

    // HTTP状态码处理
    if (error.response) {
      const message = error.response.data?.message || '请求失败'

      switch (error.response.status) {
        case 401:
          ElMessage.error(message || '登录已过期，请重新登录')
          userStore.logout()
          router.push({ path: '/login', query: { redirect: router.currentRoute.value.fullPath } })
          break
        case 403:
          ElMessage.error(message || '没有操作权限')
          router.push('/403')
          break
        case 404:
          router.push('/404')
          break
        case 500:
        case 502:
        case 503:
          ElMessage.error(message || '服务器错误，请稍后再试')
          break
        default:
          ElMessage.error(message)
      }
    } else {
      // 处理断网情况
      if (!window.navigator.onLine) {
        ElMessage.error('网络连接已断开，请检查网络设置')
      } else {
        ElMessage.error('请求超时，请稍后再试')
      }
    }

    return Promise.reject(error)
  }
)

export default service