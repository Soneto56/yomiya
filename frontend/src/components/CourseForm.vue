<template>
  <el-form
    :model="form"
    :rules="rules"
    ref="formRef"
    label-width="120px"
  >
    <el-form-item label="课程编号" prop="courseId">
      <el-input v-model="form.courseId" :disabled="!!form.courseId" />
    </el-form-item>
    <el-form-item label="课程名称" prop="courseName">
      <el-input v-model="form.courseName" />
    </el-form-item>
    <el-form-item label="学分" prop="credit">
      <el-input-number
        v-model="form.credit"
        :min="0.5"
        :step="0.5"
        :precision="1"
      />
    </el-form-item>
    <el-form-item label="学时" prop="hours">
      <el-input-number v-model="form.hours" :min="1" />
    </el-form-item>
    <el-form-item label="所属院系" prop="departmentId">
      <el-select
        v-model="form.departmentId"
        placeholder="请选择院系"
        clearable
      >
        <el-option
          v-for="dept in departments"
          :key="dept.departmentId"
          :label="dept.departmentName"
          :value="dept.departmentId"
        />
      </el-select>
    </el-form-item>
    <el-form-item label="课程描述" prop="description">
      <el-input v-model="form.description" type="textarea" rows="4" />
    </el-form-item>
  </el-form>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import { getDepartments } from '@/api/department'

const props = defineProps({
  initialData: {
    type: Object,
    default: () => ({
      courseId: '',
      courseName: '',
      credit: 2,
      hours: 32,
      departmentId: null,
      description: ''
    })
  }
})

const emit = defineEmits(['submit'])

const form = ref({ ...props.initialData })
const formRef = ref(null)
const departments = ref([])

// 加载院系列表
const loadDepartments = async () => {
  try {
    const res = await getDepartments()
    departments.value = res.data
  } catch (error) {
    console.error('加载院系失败:', error)
  }
}

// 表单验证规则
const rules = {
  courseId: [
    { required: true, message: '请输入课程编号', trigger: 'blur' },
    { pattern: /^[A-Z]{3}\d{3}$/, message: '格式如: CSC101' }
  ],
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  departmentId: [
    { required: true, message: '请选择院系', trigger: 'change' }
  ]
}

// 初始化加载
loadDepartments()

// 暴露表单验证方法
defineExpose({
  validate: () => formRef.value.validate().then(() => {
    emit('submit', form.value)
    return true
  }).catch(() => false)
})
</script>