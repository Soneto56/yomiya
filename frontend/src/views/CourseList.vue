<template>
  <div>
    <h1>课程列表</h1>
    <el-table :data="courses" stripe>
      <el-table-column prop="courseId" label="课程编号"></el-table-column>
      <el-table-column prop="courseName" label="课程名称"></el-table-column>
      <el-table-column prop="credit" label="学分"></el-table-column>
      <el-table-column prop="hours" label="学时"></el-table-column>
      <el-table-column prop="department.departmentName" label="所属院系"></el-table-column>
      <el-table-column label="操作">
        <template #default="scope">
          <el-button type="primary" @click="goToDetail(scope.row.id)">详情</el-button>
          <el-button type="warning" @click="goToEdit(scope.row.id)">编辑</el-button>
          <el-button type="danger" @click="deleteCourse(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-button @click="createCourse">创建课程</el-button>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getCourses, deleteCourse } from '@/api/course'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const courses = ref([])

const fetchCourses = async () => {
  try {
    const res = await getCourses()
    courses.value = res.data
  } catch (error) {
    ElMessage.error('获取课程列表失败')
  }
}

const goToDetail = (id) => {
  router.push(`/courses/${id}`)
}

const goToEdit = (id) => {
  router.push(`/courses/${id}/edit`)
}

const createCourse = () => {
  router.push('/courses/create')
}

const deleteCourseConfirm = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCourse(id)
    ElMessage.success('课程删除成功')
    fetchCourses()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('课程删除失败')
    }
  }
}

onMounted(fetchCourses)
</script>

<style scoped>
/* 这里可以添加样式 */
</style>