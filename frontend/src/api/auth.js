// src/api/auth.js
export const login = async (credentials) => {
  // 实现登录逻辑
  return {
    token: 'example-token',
    role: 'user'
  };
};

export const logout = async () => {
  // 实现登出逻辑
};

export const getProfile = async () => {
  // 实现获取用户信息逻辑
  return {
    role: 'user'
  };
};