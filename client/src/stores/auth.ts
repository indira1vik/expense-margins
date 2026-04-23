import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import authApi, { type AuthResponse } from '@/api/auth.api'
import type { UserProfile } from '@/api/user.api'

export const useAuthStore = defineStore('auth', () => {
  const userId = ref<string | null>(localStorage.getItem('userId'))
  const userProfile = ref<UserProfile | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => !!userId.value)

  const register = async (name: string, email: string, password: string) => {
    isLoading.value = true
    error.value = null
    try {
      const response: AuthResponse = await authApi.register({ name, email, password })
      userId.value = response.userId
      localStorage.setItem('userId', response.userId)
      return response
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Registration failed'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const login = async (email: string, password: string) => {
    isLoading.value = true
    error.value = null
    try {
      const response: AuthResponse = await authApi.login({ email, password })
      userId.value = response.userId
      localStorage.setItem('userId', response.userId)
      return response
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Login failed'
      throw err
    } finally {
      isLoading.value = false
    }
  }

  const logout = () => {
    userId.value = null
    userProfile.value = null
    localStorage.removeItem('userId')
    error.value = null
  }

  const setUserProfile = (profile: UserProfile) => {
    userProfile.value = profile
  }

  const clearError = () => {
    error.value = null
  }

  return {
    userId,
    userProfile,
    isLoading,
    error,
    isAuthenticated,
    register,
    login,
    logout,
    setUserProfile,
    clearError
  }
})
