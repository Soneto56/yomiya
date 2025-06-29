<template>
  <div>
    <h1>创建课程</h1>
    <CourseForm @submit="handleSubmit" />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import CourseForm from '@/components/CourseForm.vue'
import { createCourse } from '@/api/course'
import { ElMessage } from 'element-plus'

const router = useRouter()

const handleSubmit = async (formData) => {
  try {
    await createCourse(formData)
    ElMessage.success('课程创建成功')
    router.push('/courses')
  } catch (error) {
    ElMessage.error('课程创建失败')
  }
}
</script>

<style scoped>
/* 这里可以添加样式 */
</style>