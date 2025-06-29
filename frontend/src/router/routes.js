// routes.js
export default [
  {
    path: '/',
    redirect: '/login', // 重定向到登录页
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
    path: '/courses',
    name: 'CourseList',
    component: () => import('@/views/CourseList.vue'),
    meta: {
      requiresAuth: true,
      title: '课程列表',
      roles: ['ADMIN', 'USER'] // 允许访问的角色
    }
  },
  {
    path: '/courses/create',
    name: 'CourseCreate',
    component: () => import('@/views/CourseCreate.vue'),
    meta: {
      requiresAuth: true,
      title: '创建课程',
      roles: ['ADMIN'] // 仅管理员可访问
    }
  },
  {
    path: '/courses/:id',
    name: 'CourseDetail',
    component: () => import('@/views/CourseDetail.vue'),
    meta: {
      requiresAuth: true,
      title: '课程详情',
      roles: ['ADMIN', 'USER']
    }
  },
  {
    path: '/courses/:id/edit',
    name: 'CourseEdit',
    component: () => import('@/views/CourseEdit.vue'),
    meta: {
      requiresAuth: true,
      title: '编辑课程',
      roles: ['ADMIN']
    }
  },
  {
    path: '/403',
    name: 'Forbidden',
    component: () => import('@/views/403.vue'),
    meta: {
      title: '无权限访问'
    }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/404.vue'),
    meta: {
      title: '页面未找到'
    }
  },
  {
    path: '/:catchAll(.*)', // 匹配所有未定义的路由
    redirect: '/404'
  }
]