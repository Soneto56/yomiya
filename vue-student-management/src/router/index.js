// src/router/index.js
import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';
import CourseTableView from '../views/CourseTableView.vue';
import CourseSelectionView from '../views/CourseSelectionView.vue';

const routes = [
  {
    path: '/',
    redirect: '/login' // 根路径重定向到登录页面
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView
  },
  {
    path: '/register',
    name: 'Register',
    component: RegisterView
  },
  {
    path: '/course-table',
    name: 'CourseTable',
    component: CourseTableView
  },
  {
    path: '/course-selection',
    name: 'CourseSelection',
    component: CourseSelectionView
  }
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
});

export default router;