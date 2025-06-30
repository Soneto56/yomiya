// src/api/auth.js
import request from '@/utils/request';

export const login = async (credentials) => {
  try {
    const { username } = credentials;
    const res = await request.post('/api/login', { username });
    return res.data;
  } catch (error) {
    console.error('登录请求失败:', error);
    throw error;
  }
};

export const logout = async () => {
  try {
    await request.post('/api/logout');
  } catch (error) {
    console.error('登出请求失败:', error);
    throw error;
  }
};

export const getProfile = async () => {
  try {
    const res = await request.get('/api/profile');
    return res.data;
  } catch (error) {
    console.error('获取用户信息请求失败:', error);
    throw error;
  }
};

// 修改注册方法，区分学生和教师
export const registerStudent = async (studentInfo) => {
  try {
    const res = await request.post('/students/register', studentInfo);
    return res.data;
  } catch (error) {
    console.error('学生注册请求失败:', error);
    throw error;
  }
};

export const registerTeacher = async (teacherInfo) => {
  try {
    const res = await request.post('/teachers/register', teacherInfo);
    return res.data;
  } catch (error) {
    console.error('教师注册请求失败:', error);
    throw error;
  }
};