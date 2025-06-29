<template>
  <el-card class="course-detail" v-loading="loading">
    <template #header>
      <div class="card-header">
        <h2>{{ course.courseName }} ({{ course.courseId }})</h2>
        <div>
          <el-tag type="info">{{ course.department?.departmentName }}</el-tag>
          <el-tag :type="course.status ? 'success' : 'warning'" style="margin-left: 8px">
            {{ course.status ? '启用中' : '已停用' }}
          </el-tag>
        </div>
      </div>
    </template>

    <el-descriptions :column="2" border>
      <el-descriptions-item label="学分">{{ course.credit }}</el-descriptions-item>
      <el-descriptions-item label="学时">{{ course.hours }}</el-descriptions-item>
      <el-descriptions-item label="授课教师">{{ course.teacher || '未分配' }}</el-descriptions-item>
      <el-descriptions-item label="开课学期">{{ course.semester || '未设置' }}</el-descriptions-item>
      <el-descriptions-item label="课程描述" :span="2">
        {{ course.description || '暂无描述' }}
      </el-descriptions-item>
    </el-descriptions>

    <div class="actions">
      <el-button type="primary" @click="handleEdit">编辑</el-button>
      <el-button type="danger" @click="handleToggleStatus">
        {{ course.status ? '停用课程' : '启用课程' }}
      </el-button>
      <el-button @click="$router.go(-1)">返回</el-button>
    </div>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getCourseById, toggleCourseStatus } from '@/api/course'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const course = ref({
  courseId: '',
  courseName: '',
  credit: 0,
  hours: 0,
  department: {},
  description: '',
  status: true,
  teacher: '',
  semester: ''
})

const fetchCourse = async () => {
  loading.value = true
  try {
    const res = await getCourseById(route.params.id)
    course.value = res.data
  } catch (error) {
    ElMessage.error('获取课程详情失败')
    console.error('获取课程详情失败:', error)
  } finally {
    loading.value = false
  }
}

const handleEdit = () => {
  router.push(`/courses/${course.value.courseId}/edit`)
}

const handleToggleStatus = async () => {
  try {
    await ElMessageBox.confirm(
      `确定要${course.value.status ? '停用' : '启用'}该课程吗?`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    await toggleCourseStatus(course.value.courseId)
    course.value.status = !course.value.status
    ElMessage.success('操作成功')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
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