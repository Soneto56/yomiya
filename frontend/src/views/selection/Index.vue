<template>
  <div class="selection-management">
    <el-table :data="selections" border>
      <el-table-column prop="student.name" label="学生姓名" />
      <el-table-column prop="course.courseName" label="课程名称" />
      <el-table-column prop="teacher.name" label="授课教师" />
      <el-table-column prop="semester" label="学期" />
      <el-table-column label="成绩">
        <template #default="{row}">
          <el-tag v-if="row.score" type="success">{{ row.score }}</el-tag>
          <el-tag v-else type="warning">未评分</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" v-if="isTeacher">
        <template #default="{row}">
          <el-button
            size="small"
            @click="handleGrade(row)"
            :disabled="!!row.score"
          >
            评分
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useSelectionStore } from '@/stores/selection'
import { useUserStore } from '@/stores/user'

const selectionStore = useSelectionStore()
const userStore = useUserStore()

const selections = ref([])
const isTeacher = computed(() => userStore.role === 'TEACHER')

// 加载选课数据
const fetchSelections = async () => {
  if (isTeacher.value) {
    selections.value = await selectionStore.getByTeacher(userStore.userId)
  } else {
    selections.value = await selectionStore.getAll()
  }
}

// 评分操作
const handleGrade = (row) => {
  ElMessageBox.prompt('请输入成绩', '评分', {
    inputPattern: /^[0-9]{1,3}$/,
    inputErrorMessage: '请输入0-100的整数'
  }).then(async ({ value }) => {
    await selectionStore.updateScore(row.selectionId, value)
    ElMessage.success('评分成功')
    fetchSelections()
  })
}
</script>