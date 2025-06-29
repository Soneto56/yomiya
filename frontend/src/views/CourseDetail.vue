<template>
  <div>
    <h1>课程详情</h1>
    <el-card v-if="course">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="课程编号">{{ course.courseId }}</el-descriptions-item>
        <el-descriptions-item label="课程名称">{{ course.courseName }}</el-descriptions-item>
        <el-descriptions-item label="学分">{{ course.credit }}</el-descriptions-item>
        <el-descriptions-item label="学时">{{ course.hours }}</el-descriptions-item>
        <el-descriptions-item label="所属院系">{{ course.department?.departmentName }}</el-descriptions-item>
        <el-descriptions-item label="课程描述">{{ course.description }}</el-descriptions-item>
      </el-descriptions>
      <el-button @click="$router.back()">返回</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getCourseById } from '@/api/course'
import { ElMessage } from 'element-plus'

const route = useRoute()
const course = ref(null)

const fetchCourse = async () => {
  try {
    const res = await getCourseById(route.params.id)
    course.value = res.data
  } catch (error) {
    ElMessage.error('获取课程详情失败')
  }
}

onMounted(fetchCourse)
</script>

<style scoped>
/* 这里可以添加样式 */
</style>