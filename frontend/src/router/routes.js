export default [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue')
  },
  {
    path: '/courses',
    name: 'CourseManagement',
    component: () => import('@/views/course/Index.vue'),
    meta: {
      requiresAuth: true,
      roles: ['ADMIN'],
      title: '课程管理'
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: {
      guestOnly: true,
      title: '登录'
    }
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/error/403.vue')
  }
]