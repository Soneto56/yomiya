import request from '@/utils/request'

export const getCourses = (params) => {
  return request({
    url: '/api/courses',
    method: 'get',
    params
  })
}

export const getCourseById = (id) => {
  return request.get(`/api/courses/${id}`)
}

export const createCourse = (data) => {
  return request.post('/api/courses', data)
}

export const updateCourse = (id, data) => {
  return request.put(`/api/courses/${id}`, data)
}

export const deleteCourse = (id) => {
  return request.delete(`/api/courses/${id}`)
}

export const searchCourses = (keyword) => {
  return request.get('/api/courses/search', {
    params: { keyword }
  })
}