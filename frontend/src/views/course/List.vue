<template>
  <div class="course-list">
    <div class="header">
      <el-button type="primary" @click="handleCreate">新增课程</el-button>
      <el-input
        v-model="searchQuery"
        placeholder="搜索课程名称或编号"
        style="width: 300px; margin-left: 20px"
        clearable
        @clear="handleSearch"
        @keyup.enter="handleSearch"
      >
        <template #append>
          <el-button @click="handleSearch">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
    </div>

    <el-table
      :data="filteredCourses"
      border
      v-loading="loading"
      style="margin-top: 20px"
    >
      <el-table-column prop="courseId" label="课程编号" width="120" sortable />
      <el-table-column prop="courseName" label="课程名称" sortable />
      <el-table-column prop="credit" label="学分" width="80" sortable />
      <el-table-column prop="hours" label="学时" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{row}">
          <el-tag :type="row.status ? 'success' : 'warning'">
            {{ row.status ? '启用' : '停用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{row}">
          <el-button size="small" @click="handleView(row)">查看</el-button>
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row)"
            :disabled="!isAdmin"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCourseStore } from '@/stores/courseStore'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user' // 引入用户状态管理

const router = useRouter()
const store = useCourseStore()
const userStore = useUserStore() // 获取用户状态
const loading = ref(false)
const searchQuery = ref('')

const isAdmin = computed(() => userStore.role === 'ADMIN') // 计算属性判断是否为管理员

const filteredCourses = computed(() => {
  if (!searchQuery.value) return store.courses
  const query = searchQuery.value.toLowerCase()
  return store.courses.filter(
    course =>
      course.courseName.toLowerCase().includes(query) ||
      course.courseId.toLowerCase().includes(query)
  )
})

const fetchCourses = async () => {
  loading.value = true
  try {
    await store.fetchCourses()
  } catch (error) {
    ElMessage.error(`获取课程列表失败: ${error.message || '未知错误'}`)
    console.error('获取课程列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleCreate = () => {
  router.push('/courses/create')
}

const handleView = (row) => {
  router.push(`/courses/${row.courseId}`)
}

const handleEdit = (row) => {
  router.push(`/courses/${row.courseId}/edit`)
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该课程吗?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await store.deleteCourse(row.courseId) // 调用 store 中的删除方法
    ElMessage.success('删除成功')
    await fetchCourses() // 刷新列表
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`删除失败: ${error.message || '未知错误'}`)
      console.error('删除课程失败:', error)
    }
  }
}

const handleSearch = () => {
  // 搜索逻辑已在computed属性中处理
}

onMounted(fetchCourses)
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

/* 可以添加更多样式优化，比如表格的样式 */
.el-table {
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>