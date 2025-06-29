<template>
  <div class="selection-management">
    <div class="header">
      <h2>选课管理</h2>
      <div class="actions">
        <el-input
          v-model="searchQuery"
          placeholder="搜索学生/课程"
          style="width: 300px"
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
        <el-select
          v-model="semesterFilter"
          placeholder="选择学期"
          clearable
          style="margin-left: 10px; width: 150px"
          @change="handleSearch"
        >
          <el-option
            v-for="semester in uniqueSemesters"
            :key="semester"
            :label="semester"
            :value="semester"
          />
        </el-select>
      </div>
    </div>

    <el-table
      :data="filteredSelections"
      border
      v-loading="loading"
      style="margin-top: 20px"
      @sort-change="handleSortChange"
    >
      <el-table-column
        prop="student.name"
        label="学生姓名"
        sortable
        width="120"
      />
      <el-table-column
        prop="student.studentId"
        label="学号"
        sortable
        width="120"
      />
      <el-table-column
        prop="course.courseName"
        label="课程名称"
        sortable
        width="180"
      />
      <el-table-column
        prop="course.courseId"
        label="课程编号"
        sortable
        width="120"
      />
      <el-table-column
        prop="teacher.name"
        label="授课教师"
        sortable
        width="120"
      />
      <el-table-column prop="semester" label="学期" sortable width="120" />
      <el-table-column label="成绩" sortable prop="score" width="120">
        <template #default="{row}">
          <el-tag v-if="row.score" :type="getScoreTagType(row.score)">
            {{ row.score }}
          </el-tag>
          <el-tag v-else type="warning">未评分</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" v-if="isTeacher || isAdmin">
        <template #default="{row}">
          <el-button
            size="small"
            @click="handleGrade(row)"
            :disabled="!!row.score && !isAdmin"
          >
            {{ row.score ? '修改' : '评分' }}
          </el-button>
          <el-button
            size="small"
            type="danger"
            @click="handleDelete(row)"
            v-if="isAdmin"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="gradeDialogVisible" title="成绩录入" width="30%">
      <el-form :model="gradeForm" label-width="80px">
        <el-form-item label="学生姓名">
          <el-input v-model="currentSelection.student.name" disabled />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="currentSelection.course.courseName" disabled />
        </el-form-item>
        <el-form-item label="成绩" required>
          <el-input-number
            v-model="gradeForm.score"
            :min="0"
            :max="100"
            controls-position="right"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitGrade">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useSelectionStore } from '@/stores/selection'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'

const selectionStore = useSelectionStore()
const userStore = useUserStore()

const selections = ref([])
const loading = ref(false)
const searchQuery = ref('')
const semesterFilter = ref('')
const gradeDialogVisible = ref(false)
const currentSelection = ref({})
const gradeForm = ref({ score: null })

const isTeacher = computed(() => userStore.role === 'TEACHER')
const isAdmin = computed(() => userStore.role === 'ADMIN')

const uniqueSemesters = computed(() => {
  const semesters = new Set()
  selections.value.forEach((s) => semesters.add(s.semester))
  return Array.from(semesters).sort().reverse()
})

const filteredSelections = computed(() => {
  let result = selections.value
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(
      (s) =>
        s.student.name.toLowerCase().includes(query) ||
        s.student.studentId.toLowerCase().includes(query) ||
        s.course.courseName.toLowerCase().includes(query) ||
        s.course.courseId.toLowerCase().includes(query)
    )
  }
  if (semesterFilter.value) {
    result = result.filter((s) => s.semester === semesterFilter.value)
  }
  return result
})

const getScoreTagType = (score) => {
  if (score >= 90) return 'success'
  if (score >= 60) return ''
  return 'danger'
}

// 加载选课数据
const fetchSelections = async () => {
  loading.value = true
  try {
    if (isTeacher.value) {
      selections.value = await selectionStore.getByTeacher(userStore.userId)
    } else if (isAdmin.value) {
      selections.value = await selectionStore.getAll()
    } else {
      selections.value = await selectionStore.getByStudent(userStore.userId)
    }
  } catch (error) {
    ElMessage.error('获取选课数据失败')
    console.error('获取选课数据失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  // 搜索逻辑已在computed属性中处理
}

const handleSortChange = ({ prop, order }) => {
  // 可以在这里添加排序逻辑或直接让el-table处理
}

const handleGrade = (row) => {
  currentSelection.value = row
  gradeForm.value.score = row.score || null
  gradeDialogVisible.value = true
}

const submitGrade = async () => {
  try {
    await selectionStore.updateScore(
      currentSelection.value.selectionId,
      gradeForm.value.score
    )
    ElMessage.success('成绩提交成功')
    gradeDialogVisible.value = false
    await fetchSelections()
  } catch (error) {
    ElMessage.error('成绩提交失败')
    console.error('成绩提交失败:', error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除这条选课记录吗?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await selectionStore.deleteSelection(row.selectionId)
    ElMessage.success('删除成功')
    await fetchSelections()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(fetchSelections)
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.actions {
  display: flex;
  align-items: center;
}
</style>