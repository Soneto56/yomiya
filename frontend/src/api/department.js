import request from '@/utils/request'

export const getDepartments = () => {
  return request.get('/api/departments')
}