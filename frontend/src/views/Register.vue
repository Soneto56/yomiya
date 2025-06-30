<template>
  <div class="register-container">
    <el-card class="register-box">
      <div class="register-header">
        <img src="@/assets/logo.png" alt="学校Logo" class="logo" />
        <h2>教务管理系统 - 注册</h2>
      </div>

      <!-- 添加用户类型选择 -->
      <el-radio-group v-model="userType" size="large" class="mb-4">
        <el-radio-button label="student">学生注册</el-radio-button>
        <el-radio-button label="teacher">教师注册</el-radio-button>
      </el-radio-group>

      <el-form
        :model="form"
        :rules="rules"
        ref="registerFormRef"
        @keyup.enter="handleRegister"
        status-icon
      >
        <el-form-item prop="username">
          <el-input
            v-model="form.username"
            placeholder="请输入学号/工号"
            prefix-icon="User"
            clearable
            @focus="resetFieldError('username')"
          />
        </el-form-item>

        <el-form-item prop="name">
          <el-input
            v-model="form.name"
            placeholder="请输入姓名"
            prefix-icon="User"
            clearable
            @focus="resetFieldError('name')"
          />
        </el-form-item>

        <el-form-item prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
            prefix-icon="Mail"
            clearable
            @focus="resetFieldError('email')"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleRegister"
            :loading="loading"
            style="width: 100%"
          >
            {{ loading ? '注册中...' : '注 册' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="register-footer">
        <el-link type="primary" @click="goToLogin">已有账号，去登录</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { registerStudent, registerTeacher } from '@/api/auth';

const router = useRouter();
const registerFormRef = ref(null);

// 新增：用户类型（student/teacher）
const userType = ref('student');

// 注册表单（添加name字段）
const form = ref({
  username: '',
  name: '',
  email: ''
});

const loading = ref(false);

// 验证规则（添加name字段验证）
const rules = {
  username: [
    { required: true, message: '请输入学号/工号', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{6,20}$/, message: '账号格式不正确', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
  ]
};

// 重置字段错误状态
const resetFieldError = (field) => {
  if (registerFormRef.value) {
    registerFormRef.value.clearValidate(field);
  }
};

// 处理注册
const handleRegister = async () => {
  try {
    await registerFormRef.value.validate();
    loading.value = true;

    let res;
    // 根据用户类型调用不同的注册接口
    if (userType.value === 'student') {
      res = await registerStudent({
        studentId: form.value.username,
        name: form.value.name,
        email: form.value.email
      });
    } else {
      res = await registerTeacher({
        teacherId: form.value.username,
        name: form.value.name,
        email: form.value.email
      });
    }

    ElMessage.success('注册成功，请登录');
    router.push('/login');
  } catch (error) {
    ElMessage.error(error.response?.data?.message || '注册失败，请检查信息');
  } finally {
    loading.value = false;
  }
};

// 前往登录页
const goToLogin = () => {
  router.push('/login');
};

onMounted(() => {
  // 可添加初始化逻辑
});
</script>

<style scoped>
/* 样式不变 */
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: url('@/assets/login-bg.jpg') no-repeat center center;
  background-size: cover;
}

.register-box {
  width: 420px;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

/* 其他样式保持不变 */
</style>