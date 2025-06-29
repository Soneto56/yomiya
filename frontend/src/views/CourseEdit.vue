<template>
  <div>
    <h1>编辑课程</h1>
    <CourseForm :initialData="course" @submit="handleSubmit" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import CourseForm from '@/components/CourseForm.vue'
import { getCourseById, updateCourse } from '@/api/course'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const course = ref({})

const fetchCourse = async () => {
  try {
    const res = await getCourseById(route.params.id)
    course.value = res.data
  } catch (error) {
    ElMessage.error('获取课程信息失败')
  }
}

const handleSubmit = async (formData) => {
  try {
    await updateCourse(route.params.id, formData)
    ElMessage.success('课程更新成功')
    router.push('/courses')
  } catch (error) {
    ElMessage.error('课程更新失败')
  }
}

onMounted(fetchCourse)
</script>

<style scoped>
/* 这里可以添加样式 */
</style>