import { defineStore } from 'pinia'
import { getCourses } from '@/api/course'

export const useCourseStore = defineStore('course', {
  state: () => ({
    courses: [],
    currentCourse: null
  }),
  actions: {
    async fetchCourses() {
      try {
        const res = await getCourses()
        this.courses = res.data
      } catch (error) {
        console.error('获取课程列表失败:', error)
      }
    }
  }
})