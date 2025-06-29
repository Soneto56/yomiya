// src/api/auth.js
import request from '@/utils/request';

// 登录接口
export const login = async (credentials) => {
  try {
    const res = await request.post('/api/login', credentials);
    return res.data;
  } catch (error) {
    console.error('登录请求失败:', error);
    throw error;
  }
};

// 登出接口
export const logout = async () => {
  try {
    await request.post('/api/logout');
  } catch (error) {
    console.error('登出请求失败:', error);
    throw error;
  }
};

// 获取用户信息接口
export const getProfile = async () => {
  try {
    const res = await request.get('/api/profile');
    return res.data;
  } catch (error) {
    console.error('获取用户信息请求失败:', error);
    throw error;
  }
};