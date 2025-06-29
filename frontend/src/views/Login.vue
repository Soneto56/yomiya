<template>
  <div class="login-container">
    <el-card class="login-box">
      <div class="login-header">
        <img src="@/assets/logo.png" alt="学校Logo" class="logo" />
        <h2>教务管理系统</h2>
      </div>

      <el-form
        :model="form"
        :rules="rules"
        ref="loginFormRef"
        @keyup.enter="handleLogin"
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

        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            show-password
            @focus="resetFieldError('password')"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleLogin"
            :loading="loading"
            style="width: 100%"
          >
            {{ loading ? '登录中...' : '登 录' }}
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        <el-checkbox v-model="rememberMe">记住账号</el-checkbox>
        <el-link type="primary" @click="showForgotDialog">忘记密码?</el-link>
      </div>
    </el-card>

    <!-- 忘记密码对话框 -->
    <el-dialog v-model="forgotDialogVisible" title="找回密码" width="30%">
      <el-form :model="forgotForm" label-width="100px">
        <el-form-item label="学号/工号">
          <el-input v-model="forgotForm.username" />
        </el-form-item>
        <el-form-item label="验证邮箱">
          <el-input v-model="forgotForm.email" />
        </el-form-item>
        <el-form-item label="验证码">
          <div style="display: flex">
            <el-input v-model="forgotForm.verifyCode" />
            <el-button style="margin-left: 10px">获取验证码</el-button>
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="forgotDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleResetPassword">提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';
import request from '@/utils/request';

const router = useRouter();
const userStore = useUserStore();
const loginFormRef = ref(null);

// 登录表单
const form = ref({
  username: localStorage.getItem('rememberedUsername') || '',
  password: '',
});

// 记住我功能
const rememberMe = ref(localStorage.getItem('rememberMe') === 'true');

// 忘记密码相关
const forgotDialogVisible = ref(false);
const forgotForm = ref({
  username: '',
  email: '',
  verifyCode: ''
});

const loading = ref(false);
const errorCount = ref(0);

// 验证规则
const rules = {
  username: [
    { required: true, message: '请输入学号/工号', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{6,20}$/, message: '账号格式不正确', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6 - 20个字符', trigger: 'blur' }
  ]
};

// 重置字段错误状态
const resetFieldError = (field) => {
  if (loginFormRef.value) {
    loginFormRef.value.clearValidate(field);
  }
};

// 处理登录
const handleLogin = async () => {
  try {
    await loginFormRef.value.validate();
    loading.value = true;

    // 记住用户名
    if (rememberMe.value) {
      localStorage.setItem('rememberedUsername', form.value.username);
      localStorage.setItem('rememberMe', 'true');
    } else {
      localStorage.removeItem('rememberedUsername');
      localStorage.removeItem('rememberMe');
    }

    // 登录请求
    await userStore.login({
      username: form.value.username,
      password: form.value.password
    });

    // 登录成功后跳转
    const redirect = router.currentRoute.value.query.redirect || '/dashboard';
    router.push(redirect);

  } catch (error) {
    if (error !== 'cancel') {
      errorCount.value++;
      ElMessage.error(error.response?.data?.message || '登录失败，请检查账号密码');
    }
  } finally {
    loading.value = false;
  }
};

// 忘记密码相关方法
const showForgotDialog = () => {
  forgotForm.value.username = form.value.username;
  forgotDialogVisible.value = true;
};

const handleResetPassword = async () => {
  try {
    // 调用 API 重置密码
    await request.post('/api/reset-password', forgotForm.value);
    ElMessage.success('密码重置邮件已发送，请查收');
    forgotDialogVisible.value = false;
  } catch (error) {
    ElMessage.error('密码重置失败: ' + (error.response?.data?.message || error.message));
  }
};

onMounted(() => {
  // 如果有记住的用户名，自动聚焦密码框
  if (form.value.username) {
    setTimeout(() => {
      document.querySelector('input[type=password]')?.focus();
    }, 100);
  }
});
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: url('@/assets/login-bg.jpg') no-repeat center center;
  background-size: cover;
}

.login-box {
  width: 420px;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header .logo {
  height: 60px;
  margin-bottom: 15px;
}

.login-header h2 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.login-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}
</style>