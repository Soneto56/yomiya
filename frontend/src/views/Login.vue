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
        <el-link type="primary" @click="goToRegister">注册账号</el-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';

const router = useRouter();
const userStore = useUserStore();
const loginFormRef = ref(null);

// 登录表单
const form = ref({
  username: localStorage.getItem('rememberedUsername') || ''
});

// 记住我功能
const rememberMe = ref(localStorage.getItem('rememberMe') === 'true');

const loading = ref(false);

// 验证规则
const rules = {
  username: [
    { required: true, message: '请输入学号/工号', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9]{6,20}$/, message: '账号格式不正确', trigger: 'blur' }
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

    // 检查是否为测试账号 123456789
    if (form.value.username === '123456789') {
      // 模拟登录成功，设置token和角色
      userStore.token = 'test_token';
      userStore.role = 'USER';
      localStorage.setItem('token', 'test_token');
      // 模拟获取用户信息
      userStore.userInfo = { username: '123456789', role: 'USER' };

      // 登录成功后跳转
      const redirect = router.currentRoute.value.query.redirect || '/dashboard';
      router.push(redirect);
    } else {
      // 正常登录请求，仅传递学号
      await userStore.login({
        username: form.value.username
      });

      // 登录成功后跳转
      const redirect = router.currentRoute.value.query.redirect || '/dashboard';
      router.push(redirect);
    }
  } catch (error) {
    if (error!== 'cancel') {
      ElMessage.error(error.response?.data?.message || '登录失败，请检查账号');
    }
  } finally {
    loading.value = false;
  }
};

// 跳转到注册页面
const goToRegister = () => {
  router.push('/register');
};

onMounted(() => {
  // 如果有记住的用户名，自动聚焦输入框
  if (form.value.username) {
    setTimeout(() => {
      document.querySelector('input[type=text]')?.focus();
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

.login-header.logo {
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