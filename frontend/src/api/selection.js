import request from '@/utils/request'

export const getSelectionsByCourse = (courseId) => {
  return request.get(`/api/courseSelections/course/${courseId}`)
}