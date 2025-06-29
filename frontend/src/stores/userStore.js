import { defineStore } from 'pinia'
import { login, logout, getProfile } from '@/api/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    userInfo: null,
    role: null
  }),

  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.role === 'ADMIN'
  },

  actions: {
    async login(credentials) {
      const res = await login(credentials)
      this.token = res.token
      this.role = res.role
      localStorage.setItem('token', res.token)
      await this.fetchProfile()
    },

    async fetchProfile() {
      this.userInfo = await getProfile()
    },

    logout() {
      this.token = null
      this.userInfo = null
      localStorage.removeItem('token')
    }
  }
})