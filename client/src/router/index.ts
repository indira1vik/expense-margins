import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/dashboard'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/signup',
    name: 'Signup',
    component: () => import('@/pages/Signup.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/pages/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/expense',
    name: 'Expense',
    component: () => import('@/pages/Expense.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/expense/create',
    name: 'ExpenseCreate',
    component: () => import('@/pages/ExpenseCreate.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/expense/:id',
    name: 'ExpenseDetail',
    component: () => import('@/pages/ExpenseDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/settlement',
    name: 'Settlement',
    component: () => import('@/pages/Settlement.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Navigation guards
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const isAuthenticated = authStore.isAuthenticated

  // If route requires auth and user is not authenticated
  if (to.meta.requiresAuth && !isAuthenticated) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // If user is already authenticated and tries to access login/signup
  if (!to.meta.requiresAuth && isAuthenticated && (to.name === 'Login' || to.name === 'Signup')) {
    next({ name: 'Dashboard' })
    return
  }

  next()
})

export default router
