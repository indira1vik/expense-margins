import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const userClient = axios.create({
  baseURL: `${API_BASE_URL}/users`,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interfaces
export interface UserProfile {
  id?: number
  userId: string
  name: string
  email: string
  avatarUrl?: string
  status: 'ACTIVE' | 'INACTIVE'
}

export interface UserProfileRequest {
  userId: string
  name: string
  email: string
  avatarUrl?: string
}

// API Methods
export const userApi = {
  getUser: async (userId: string): Promise<UserProfile> => {
    const response = await userClient.get<UserProfile>(`/${userId}`)
    return response.data
  },

  createUserProfile: async (data: UserProfileRequest): Promise<UserProfile> => {
    const response = await userClient.post<UserProfile>('', data)
    return response.data
  },

  updateUserProfile: async (userId: string, data: Partial<UserProfileRequest>): Promise<UserProfile> => {
    const response = await userClient.put<UserProfile>(`/${userId}`, data)
    return response.data
  }
}

export default userApi
