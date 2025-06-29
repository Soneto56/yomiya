<template>
  <el-card class="course-detail">
    <template #header>
      <div class="card-header">
        <h2>{{ course.courseName }} ({{ course.courseId }})</h2>
        <el-tag type="info">{{ course.department?.departmentName }}</el-tag>
      </div>
    </template>

    <el-descriptions :column="2" border>
      <el-descriptions-item label="学分">{{ course.credit }}</el-descriptions-item>
      <el-descriptions-item label="学时">{{ course.hours }}</el-descriptions-item>
      <el-descriptions-item label="课程描述" :span="2">
        {{ course.description || '暂无描述' }}
      </el-descriptions-item>
    </el-descriptions>

    <div class="actions">
      <el-button type="primary" @click="handleEdit">编辑</el-button>
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCourseById } from '@/api/course'

const route = useRoute()
const router = useRouter()
const course = ref({
  courseId: '',
  courseName: '',
  credit: 0,
  hours: 0,
  department: {},
  description: ''
})

const fetchCourse = async () => {
  try {
    const res = await getCourseById(route.params.id)
    course.value = res.data
  } catch (error) {
    console.error('获取课程详情失败:', error)
  }
}

const handleEdit = () => {
  router.push(`/courses/${course.value.courseId}/edit`)
}

onMounted(fetchCourse)
</script>

<style scoped>
.course-detail {
  max-width: 800px;
  margin: 20px auto;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.actions {
  margin-top: 20px;
  text-align: right;
}
</style>