import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, register as registerApi } from '@/api/auth'
import type { LoginRequest, RegisterRequest } from '@/types'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(localStorage.getItem('token') || '')
  const username = ref<string>(localStorage.getItem('username') || '')
  const userId = ref<number>(Number(localStorage.getItem('userId')) || 0)

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)

  // 登录方法
  const login = async (data: LoginRequest) => {
    try {
      const response = await loginApi(data)
      token.value = response.token
      username.value = response.username
      userId.value = response.userId

      // 持久化到localStorage
      localStorage.setItem('token', response.token)
      localStorage.setItem('username', response.username)
      localStorage.setItem('userId', String(response.userId))

      return response
    } catch (error) {
      throw error
    }
  }

  // 注册方法
  const register = async (data: RegisterRequest) => {
    try {
      const response = await registerApi(data)
      token.value = response.token
      username.value = response.username
      userId.value = response.userId

      // 持久化到localStorage
      localStorage.setItem('token', response.token)
      localStorage.setItem('username', response.username)
      localStorage.setItem('userId', String(response.userId))

      return response
    } catch (error) {
      throw error
    }
  }

  // 登出方法
  const logout = () => {
    token.value = ''
    username.value = ''
    userId.value = 0

    // 清除localStorage
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    localStorage.removeItem('userId')
  }

  return {
    token,
    username,
    userId,
    isLoggedIn,
    login,
    register,
    logout
  }
})
