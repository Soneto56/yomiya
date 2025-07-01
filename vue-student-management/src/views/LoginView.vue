<template>
  <div class="login-container">
    <div class="login-card">
      <div class="api-info">
        <div class="api-title">后端API集成：</div>
        <div class="api-detail">POST /api/students/login</div>
        <div class="api-detail">请求: { studentId: "学号" }</div>
        <div class="api-detail">响应: { message, student, token }</div>
      </div>

      <div class="card-header">
        <div class="system-icon">
          <i class="fas fa-graduation-cap"></i>
        </div>
        <h1>学生选课系统</h1>
        <p>请输入学号登录系统</p>
      </div>

      <div class="card-body">
        <div v-if="loginError" class="error-message">
          <i class="fas fa-exclamation-circle"></i>
          <span>{{ loginError }}</span>
        </div>

        <div class="input-group">
          <label for="studentId">学号</label>
          <div class="input-icon">
            <i class="fas fa-user"></i>
          </div>
          <input
            type="text"
            id="studentId"
            v-model="studentId"
            class="form-control"
            placeholder="请输入学号"
            @keyup.enter="login"
          >
        </div>

        <div class="remember-me">
          <input
            type="checkbox"
            id="rememberMe"
            v-model="rememberMe"
          >
          <label for="rememberMe">记住学号</label>
        </div>

        <button
          @click="login"
          :disabled="loading || !studentId"
          class="btn btn-login"
        >
          <span v-if="loading" class="loading">
            <i class="fas fa-circle-notch"></i>
          </span>
          <span>{{ loading ? '登录中...' : '登录' }}</span>
        </button>

        <button
          @click="goToRegister"
          class="btn btn-register"
        >
          转到注册界面
        </button>

        <div class="footer-links">
          <a href="#"><i class="fas fa-question-circle"></i> 忘记学号?</a>
          <a href="#"><i class="fas fa-lock"></i> 重置密码</a>
        </div>
      </div>
    </div>

    <div class="login-success" :class="{ active: showSuccess }">
      <div class="success-content">
        <div class="success-icon">
          <i class="fas fa-check-circle"></i>
        </div>
        <h2>登录成功!</h2>
        <p>欢迎 {{ studentInfo.name || studentId }} 同学</p>
        <p>学号: {{ studentId }}</p>
        <p>班级: {{ studentInfo.className || '计算机科学与技术1班' }}</p>
        <button class="btn-continue" @click="redirectToDashboard">进入选课系统</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'LoginView',
  data() {
    return {
      studentId: '',
      rememberMe: false,
      loading: false,
      loginError: '',
      showSuccess: false,
      studentInfo: {}
    };
  },
  mounted() {
    // 检查本地存储是否有记住的学号
    const rememberedStudentId = localStorage.getItem('rememberedStudentId');
    if (rememberedStudentId) {
      this.studentId = rememberedStudentId;
      this.rememberMe = true;
    }
  },
  methods: {
    async login() {
      // 重置错误信息
      this.loginError = '';

      // 验证输入
      if (!this.studentId) {
        this.loginError = '请输入学号';
        return;
      }

      if (!/^\d{8,12}$/.test(this.studentId)) {
        this.loginError = '学号应为8-12位数字';
        return;
      }

      // 模拟加载状态
      this.loading = true;

      try {
        // 模拟API调用延迟
        await new Promise(resolve => setTimeout(resolve, 1500));

        // 模拟成功响应
        this.studentInfo = {
          name: "张同学",

        };

        // 处理记住学号功能
        if (this.rememberMe) {
          localStorage.setItem('rememberedStudentId', this.studentId);
        } else {
          localStorage.removeItem('rememberedStudentId');
        }

        // 显示成功动画
        this.showSuccess = true;
      } catch (error) {
        // 处理错误
        console.error('登录失败:', error);
        this.loginError = '学号不存在或服务器错误';
      } finally {
        this.loading = false;
      }
    },
    redirectToDashboard() {
      this.showSuccess = false;
      // 路由跳转到课程表页面
      this.$router.push('/course-table');
    },
    goToRegister() {
      // 路由跳转到注册页面
      this.$router.push('/register');
    }
  }
};
</script>

<style scoped>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif;
}

.login-container {
  background: linear-gradient(135deg, #1a2980, #26d0ce);
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 20px;
  width: 100%;
  max-width: 100%;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  overflow: hidden;
  transition: all 0.4s ease;
  position: relative;
  width: 100%;
  max-width: 420px;
}

.login-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

.card-header {
  background: linear-gradient(90deg, #1a2980, #26d0ce);
  color: white;
  padding: 30px 20px;
  text-align: center;
  position: relative;
}

.card-header h1 {
  font-size: 28px;
  font-weight: 700;
  margin-bottom: 8px;
  letter-spacing: 0.5px;
}

.card-header p {
  font-size: 16px;
  opacity: 0.9;
}

.system-icon {
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #1a2980, #26d0ce);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 4px solid white;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.system-icon i {
  font-size: 28px;
}

.card-body {
  padding: 30px;
}

.input-group {
  margin-bottom: 25px;
  position: relative;
}

.input-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 600;
  color: #2d3748;
  font-size: 15px;
}

.input-icon {
  position: absolute;
  top: 42px;
  left: 15px;
  color: #4a5568;
}

.form-control {
  width: 100%;
  padding: 14px 14px 14px 45px;
  border: 2px solid #e2e8f0;
  border-radius: 12px;
  font-size: 16px;
  transition: all 0.3s;
  background-color: #f8fafc;
}

.form-control:focus {
  outline: none;
  border-color: #1a2980;
  box-shadow: 0 0 0 3px rgba(26, 41, 128, 0.2);
  background-color: white;
}

.remember-me {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
}

.remember-me input {
  width: 18px;
  height: 18px;
  margin-right: 10px;
  cursor: pointer;
}

.remember-me label {
  color: #4a5568;
  font-size: 15px;
  cursor: pointer;
}

.btn {
  display: block;
  width: 100%;
  padding: 15px;
  border: none;
  border-radius: 12px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  text-align: center;
  margin-bottom: 15px;
}

.btn-login {
  background: linear-gradient(90deg, #1a2980, #26d0ce);
  color: white;
  box-shadow: 0 4px 15px rgba(26, 41, 128, 0.4);
}

.btn-login:hover:not(:disabled) {
  background: linear-gradient(90deg, #0f1a5a, #1fb1af);
  transform: translateY(-2px);
  box-shadow: 0 7px 20px rgba(26, 41, 128, 0.5);
}

.btn-login:disabled {
  background: #a0aec0;
  cursor: not-allowed;
  opacity: 0.8;
}

.btn-register {
  background: transparent;
  color: #1a2980;
  border: 2px solid #1a2980;
}

.btn-register:hover {
  background: rgba(26, 41, 128, 0.08);
  transform: translateY(-2px);
}

.error-message {
  background-color: #fff5f5;
  color: #e53e3e;
  padding: 12px;
  border-radius: 8px;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  border-left: 4px solid #e53e3e;
  animation: fadeIn 0.3s ease;
}

.error-message i {
  margin-right: 10px;
}

.loading {
  display: inline-block;
  animation: spin 1s linear infinite;
  margin-right: 10px;
}

.footer-links {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  font-size: 14px;
}

.footer-links a {
  color: #1a2980;
  text-decoration: none;
  transition: all 0.2s;
}

.footer-links a:hover {
  color: #0f1a5a;
  text-decoration: underline;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.login-success {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  opacity: 0;
  pointer-events: none;
  transition: opacity 0.5s;
}

.login-success.active {
  opacity: 1;
  pointer-events: all;
}

.success-content {
  background: white;
  padding: 40px;
  border-radius: 20px;
  text-align: center;
  max-width: 400px;
  width: 90%;
  transform: scale(0.9);
  transition: transform 0.5s;
}

.login-success.active .success-content {
  transform: scale(1);
}

.success-icon {
  font-size: 60px;
  color: #48bb78;
  margin-bottom: 20px;
}

.success-content h2 {
  color: #2d3748;
  margin-bottom: 15px;
}

.success-content p {
  color: #4a5568;
  margin-bottom: 25px;
  line-height: 1.6;
}

.btn-continue {
  background: #48bb78;
  color: white;
  border: none;
  padding: 12px 30px;
  border-radius: 50px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s;
}

.btn-continue:hover {
  background: #38a169;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(72, 187, 120, 0.4);
}

.api-info {
  position: absolute;
  bottom: 10px;
  right: 10px;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 8px;
  padding: 10px;
  font-size: 12px;
  max-width: 300px;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.api-title {
  font-weight: bold;
  color: #1a2980;
  margin-bottom: 5px;
}

.api-detail {
  margin-bottom: 3px;
  font-family: monospace;
  font-size: 11px;
}

@media (max-width: 480px) {
  .card-body {
    padding: 25px 20px;
  }

  .card-header {
    padding: 25px 15px;
  }

  .card-header h1 {
    font-size: 24px;
  }

  .btn {
    padding: 14px;
    font-size: 16px;
  }

  .footer-links {
    flex-direction: column;
    gap: 10px;
    align-items: center;
  }

  .api-info {
    position: relative;
    margin-top: 20px;
    max-width: 100%;
  }
}
</style>