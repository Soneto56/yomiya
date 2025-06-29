<template>
  <div class="course-list">
    <el-button @click="handleCreate">新增课程</el-button>

    <el-table :data="store.courses" border>
      <el-table-column prop="courseId" label="课程编号" width="120" />
      <el-table-column prop="courseName" label="课程名称" />
      <el-table-column prop="credit" label="学分" width="80" />
      <el-table-column label="操作" width="180">
        <template #default="{row}">
          <el-button size="small" @click="handleEdit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { useCourseStore } from '@/stores/courseStore'
import { onMounted } from 'vue'

const store = useCourseStore()

onMounted(() => {
  store.fetchCourses()
})

const handleCreate = () => {
  // 打开创建对话框
}
</script>