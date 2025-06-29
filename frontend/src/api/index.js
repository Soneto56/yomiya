export * from './course'
export * from './department'
export * from './selection'
export * from './user'

// 统一错误处理封装
export const handleApiError = (error) => {
  console.error('API Error:', error)
  throw new Error(error.response?.data?.message || '请求失败')
}