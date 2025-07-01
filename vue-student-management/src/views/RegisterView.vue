<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
    <div class="bg-white rounded-2xl shadow-xl max-w-md w-full overflow-hidden transform transition-all duration-500 hover:shadow-2xl">
      <!-- 顶部装饰条 -->
      <div class="h-2 bg-gradient-to-r from-blue-500 to-indigo-600"></div>

      <!-- 注册表单卡片 -->
      <div class="p-8">
        <div class="text-center mb-8">
          <h2 class="text-3xl font-bold text-gray-800 mb-2">学生注册</h2>
          <p class="text-gray-500">请输入您的邮箱和学号完成注册</p>
        </div>

        <!-- 表单 -->
        <form @submit.prevent="handleRegister">
          <!-- 邮箱输入 -->
          <div class="mb-6">
            <label for="email" class="block text-sm font-medium text-gray-700 mb-1">邮箱</label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <i class="fa fa-envelope text-gray-400"></i>
              </div>
              <input
                type="email"
                id="email"
                v-model="email"
                class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                placeholder="请输入您的邮箱"
                @blur="checkEmail"
              >
            </div>
            <!-- 邮箱验证结果提示 -->
            <div v-if="emailExists" class="mt-2 text-sm text-red-500 flex items-center">
              <i class="fa fa-exclamation-circle mr-1"></i>
              该邮箱已被注册，对应的学号是: {{ existingStudentId }}
            </div>
          </div>

          <!-- 学号输入 -->
          <div class="mb-6">
            <label for="studentId" class="block text-sm font-medium text-gray-700 mb-1">学号</label>
            <div class="relative">
              <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <i class="fa fa-user text-gray-400"></i>
              </div>
              <input
                type="text"
                id="studentId"
                v-model="studentId"
                class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-all duration-200"
                placeholder="请输入您的学号"
                @blur="checkStudentId"
              >
            </div>
            <!-- 学号验证结果提示 -->
            <div v-if="studentIdExists" class="mt-2 text-sm text-red-500 flex items-center">
              <i class="fa fa-exclamation-circle mr-1"></i>
              该学号已被注册
            </div>
          </div>

          <!-- 注册按钮 -->
          <button
            type="submit"
            class="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-3 px-4 rounded-lg font-medium transition-all duration-300 hover:shadow-lg transform hover:-translate-y-1 disabled:opacity-50 disabled:cursor-not-allowed disabled:hover:-translate-y-0"
            :disabled="emailExists || studentIdExists || !email || !studentId"
          >
            立即注册
          </button>
        </form>

        <!-- 底部链接 -->
        <div class="mt-6 text-center">
          <span class="text-gray-500">已有账号?</span>
          <router-link to="/login" class="text-blue-600 hover:text-blue-800 font-medium ml-1 transition-colors duration-200">现在登录</router-link>
        </div>
      </div>
    </div>

    <!-- 成功提示弹窗 -->
    <div v-if="showSuccessModal" class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl p-6 max-w-sm w-full transform transition-all duration-300 scale-100">
        <div class="text-center">
          <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
            <i class="fa fa-check text-green-500 text-2xl"></i>
          </div>
          <h3 class="text-xl font-bold text-gray-800 mb-2">注册成功</h3>
          <p class="text-gray-600 mb-6">您已成功注册，请使用学号登录</p>
          <button
            @click="closeSuccessModal"
            class="bg-blue-500 text-white py-2 px-6 rounded-lg font-medium transition-all duration-200 hover:bg-blue-600"
          >
            确定
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';

export default {
  name: 'RegisterView',
  setup() {
    const router = useRouter();

    // 表单数据
    const email = ref('');
    const studentId = ref('');
    const emailExists = ref(false);
    const studentIdExists = ref(false);
    const existingStudentId = ref('');
    const showSuccessModal = ref(false);

    // 创建axios实例并配置基础URL
    const api = axios.create({
      baseURL: 'http://localhost:8081/api', // 确保与后端端口一致
      timeout: 5000
    });

    // 检查邮箱是否已存在
    const checkEmail = async () => {
      if (!email.value) return;

      try {
        const response = await api.get(`/students/check-email?email=${email.value}`);
        if (response.data.exists) {
          emailExists.value = true;
          existingStudentId.value = response.data.studentId;
        } else {
          emailExists.value = false;
          existingStudentId.value = '';
        }
      } catch (error) {
        console.error('检查邮箱失败:', error);
        emailExists.value = false;
      }
    };

    // 检查学号是否已存在
    const checkStudentId = async () => {
      if (!studentId.value) return;

      try {
        const response = await api.get(`/students/check-student-id?studentId=${studentId.value}`);
        studentIdExists.value = response.data.exists;
      } catch (error) {
        console.error('检查学号失败:', error);
        studentIdExists.value = false;
      }
    };

    // 处理注册
    const handleRegister = async () => {
      if (emailExists.value || studentIdExists.value) return;

      try {
        const response = await api.post('/students/register', {
          email: email.value,
          studentId: studentId.value
        });

        if (response.status === 201) {
          showSuccessModal.value = true;
        }
      } catch (error) {
        console.error('注册失败:', error);
        alert('注册失败: ' + (error.response?.data || error.message));
      }
    };

    // 关闭成功弹窗
    const closeSuccessModal = () => {
      showSuccessModal.value = false;
      email.value = '';
      studentId.value = '';
      emailExists.value = false;
      studentIdExists.value = false;
      existingStudentId.value = '';
      router.push('/login'); // 跳转到登录页面
    };

    return {
      email,
      studentId,
      emailExists,
      studentIdExists,
      existingStudentId,
      showSuccessModal,
      checkEmail,
      checkStudentId,
      handleRegister,
      closeSuccessModal
    };
  }
};
</script>

<style scoped>
/* 自定义动画效果 */
.fade-in {
  animation: fadeIn 0.3s ease-in-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

.min-h-screen {
  min-height: 100vh;
}

.bg-gradient-to-br {
  background-image: linear-gradient(to bottom right, #f0f9ff, #e0f2fe);
}

.from-blue-50 {
  --tw-gradient-from: #f0f9ff;
}

.to-indigo-100 {
  --tw-gradient-to: #e0e7ff;
}

.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.justify-center {
  justify-content: center;
}

.p-4 {
  padding: 1rem;
}

.bg-white {
  background-color: #fff;
}

.rounded-2xl {
  border-radius: 1rem;
}

.shadow-xl {
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.max-w-md {
  max-width: 28rem;
}

.w-full {
  width: 100%;
}

.overflow-hidden {
  overflow: hidden;
}

.transform {
  transform: translateZ(0);
}

.transition-all {
  transition-property: all;
}

.duration-500 {
  transition-duration: 500ms;
}

.hover\:shadow-2xl:hover {
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.25);
}

.h-2 {
  height: 0.5rem;
}

.bg-gradient-to-r {
  background-image: linear-gradient(to right, #3b82f6, #6366f1);
}

.from-blue-500 {
  --tw-gradient-from: #3b82f6;
}

.to-indigo-600 {
  --tw-gradient-to: #4f46e5;
}

.p-8 {
  padding: 2rem;
}

.text-center {
  text-align: center;
}

.mb-8 {
  margin-bottom: 2rem;
}

.text-3xl {
  font-size: 1.875rem;
  line-height: 2.25rem;
}

.font-bold {
  font-weight: 700;
}

.text-gray-800 {
  color: #1f2937;
}

.mb-2 {
  margin-bottom: 0.5rem;
}

.text-gray-500 {
  color: #6b7280;
}

.block {
  display: block;
}

.text-sm {
  font-size: 0.875rem;
  line-height: 1.25rem;
}

.font-medium {
  font-weight: 500;
}

.text-gray-700 {
  color: #374151;
}

.mb-1 {
  margin-bottom: 0.25rem;
}

.relative {
  position: relative;
}

.absolute {
  position: absolute;
}

.inset-y-0 {
  top: 0;
  bottom: 0;
}

.left-0 {
  left: 0;
}

.pl-3 {
  padding-left: 0.75rem;
}

.items-center {
  align-items: center;
}

.pointer-events-none {
  pointer-events: none;
}

.fa {
  display: inline-block;
  font: normal normal normal 14px/1 FontAwesome;
  font-size: inherit;
  text-rendering: auto;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.text-gray-400 {
  color: #9ca3af;
}

.w-full {
  width: 100%;
}

.pl-10 {
  padding-left: 2.5rem;
}

.pr-4 {
  padding-right: 1rem;
}

.py-3 {
  padding-top: 0.75rem;
  padding-bottom: 0.75rem;
}

.border {
  border-width: 1px;
}

.border-gray-300 {
  border-color: #d1d5db;
}

.rounded-lg {
  border-radius: 0.5rem;
}

.focus\:ring-2:focus {
  --tw-ring-offset-shadow: var(--tw-ring-inset) 0 0 0 var(--tw-ring-offset-width) var(--tw-ring-offset-color);
  --tw-ring-shadow: var(--tw-ring-inset) 0 0 0 calc(2px + var(--tw-ring-offset-width)) var(--tw-ring-color);
  box-shadow: var(--tw-ring-offset-shadow), var(--tw-ring-shadow), var(--tw-shadow, 0 0 #0000);
}

.focus\:ring-blue-500:focus {
  --tw-ring-color: rgba(59, 130, 246, 0.5);
}

.focus\:border-blue-500:focus {
  border-color: #3b82f6;
}

.transition-all {
  transition-property: all;
}

.duration-200 {
  transition-duration: 200ms;
}

.placeholder\:text-gray-400::placeholder {
  color: #9ca3af;
}

.mt-2 {
  margin-top: 0.5rem;
}

.text-red-500 {
  color: #ef4444;
}

.flex {
  display: flex;
}

.items-center {
  align-items: center;
}

.mr-1 {
  margin-right: 0.25rem;
}

.bg-gradient-to-r {
  background-image: linear-gradient(to right, var(--tw-gradient-stops));
}

.from-blue-500 {
  --tw-gradient-from: #3b82f6;
  --tw-gradient-stops: var(--tw-gradient-from), var(--tw-gradient-to, rgba(59, 130, 246, 0));
}

.to-indigo-600 {
  --tw-gradient-to: #4f46e5;
}

.text-white {
  color: #fff;
}

.px-4 {
  padding-left: 1rem;
  padding-right: 1rem;
}

.font-medium {
  font-weight: 500;
}

.hover\:shadow-lg:hover {
  box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.transform {
  transform: translateZ(0);
}

.hover\:-translate-y-1:hover {
  --tw-translate-y: -0.25rem;
  transform: translate(var(--tw-translate-x), var(--tw-translate-y)) rotate(var(--tw-rotate)) skewX(var(--tw-skew-x)) skewY(var(--tw-skew-y)) scaleX(var(--tw-scale-x)) scaleY(var(--tw-scale-y));
}

.disabled\:opacity-50:disabled {
  opacity: 0.5;
}

.disabled\:cursor-not-allowed:disabled {
  cursor: not-allowed;
}

.disabled\:hover\:-translate-y-0:hover:disabled {
  --tw-translate-y: 0;
}

.mt-6 {
  margin-top: 1.5rem;
}

.text-blue-600 {
  color: #2563eb;
}

.hover\:text-blue-800:hover {
  color: #1e40af;
}

.ml-1 {
  margin-left: 0.25rem;
}

.transition-colors {
  transition-property: background-color, border-color, color, fill, stroke;
}

.duration-200 {
  transition-duration: 200ms;
}

.fixed {
  position: fixed;
}

.inset-0 {
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
}

.bg-black {
  background-color: #000;
}

.bg-opacity-50 {
  --tw-bg-opacity: 0.5;
}

.z-50 {
  z-index: 50;
}

.bg-white {
  background-color: #fff;
}

.rounded-xl {
  border-radius: 0.75rem;
}

.p-6 {
  padding: 1.5rem;
}

.max-w-sm {
  max-width: 24rem;
}

.w-full {
  width: 100%;
}

.scale-100 {
  --tw-scale-x: 1;
  --tw-scale-y: 1;
  transform: translate(var(--tw-translate-x), var(--tw-translate-y)) rotate(var(--tw-rotate)) skewX(var(--tw-skew-x)) skewY(var(--tw-skew-y)) scaleX(var(--tw-scale-x)) scaleY(var(--tw-scale-y));
}

.mx-auto {
  margin-left: auto;
  margin-right: auto;
}

.mb-4 {
  margin-bottom: 1rem;
}

.bg-green-100 {
  background-color: #dcfce7;
}

.rounded-full {
  border-radius: 9999px;
}

.text-green-500 {
  color: #22c55e;
}

.text-2xl {
  font-size: 1.5rem;
  line-height: 2rem;
}

.text-xl {
  font-size: 1.25rem;
  line-height: 1.75rem;
}

.text-gray-600 {
  color: #4b5563;
}

.py-2 {
  padding-top: 0.5rem;
  padding-bottom: 0.5rem;
}

.px-6 {
  padding-left: 1.5rem;
  padding-right: 1.5rem;
}

.hover\:bg-blue-600:hover {
  background-color: #2563eb;
}
</style>