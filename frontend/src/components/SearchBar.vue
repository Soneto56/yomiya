<template>
  <div class="search-bar">
    <el-input
      v-model="keyword"
      placeholder="输入课程/教师/学生关键词"
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
      v-model="departmentId"
      placeholder="筛选院系"
      clearable
      @change="handleSearch"
      style="width: 200px; margin-left: 10px;"
    >
      <el-option
        v-for="dept in departmentOptions"
        :key="dept.departmentId"
        :label="dept.departmentName"
        :value="dept.departmentId"
      />
    </el-select>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDepartments } from '@/api/department'

const emit = defineEmits(['search'])

const keyword = ref('')
const departmentId = ref(null)
const departmentOptions = ref([])

const handleSearch = () => {
  emit('search', {
    keyword: keyword.value.trim(),
    departmentId: departmentId.value
  })
}

onMounted(async () => {
  const res = await getDepartments()
  departmentOptions.value = res.data
})
</script>

<style scoped>
.search-bar {
  display: flex;
  margin-bottom: 20px;
}
</style>