// src/stores/user.js
import { defineStore } from 'pinia'
import { login, logout, getProfile } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    userInfo: null,
    role: null,
    loading: false
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },

  actions: {
    async login(username) {
      this.loading = true
      try {
        const res = await login({ username })
        this.token = res.token
        this.role = res.role
        localStorage.setItem('token', res.token)
        await this.fetchProfile()
        return true
      } catch (error) {
        ElMessage.error(error.response?.data?.message || '登录失败，请检查学号')
        return false
      } finally {
        this.loading = false
      }
    },

    async fetchProfile() {
      try {
        const res = await getProfile()
        this.userInfo = res
        this.role = res.role
      } catch (error) {
        console.error('获取用户信息失败:', error)
        // 处理token无效的情况
        if (error.response?.status === 401) {
          this.logout()
        }
      }
    },

    async logout() {
      try {
        await logout()
      } catch (error) {
        console.error('API登出失败:', error)
        // 即使API失败，也清除本地状态
      } finally {
        this.token = null
        this.userInfo = null
        this.role = null
        localStorage.removeItem('token')
      }
    },

    async initialize() {
      if (this.token) {
        await this.fetchProfile()
      }
    }
  }
})