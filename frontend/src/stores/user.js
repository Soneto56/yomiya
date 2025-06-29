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
    },

    async fetchProfile() {
      this.userInfo = await getProfile()
      this.role = this.userInfo.role
    },

    logout() {
      this.token = null
      this.userInfo = null
      this.role = null
      localStorage.removeItem('token')
    },

    initialize() {
      if (this.token) this.fetchProfile()
    }
  }
})