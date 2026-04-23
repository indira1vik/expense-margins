import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const settlementClient = axios.create({
  baseURL: `${API_BASE_URL}/settlements`,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interfaces
export interface Settlement {
  id: number
  expenseId: number
  fromUserId: string
  toUserId: string
  amount: number
  status: 'PENDING' | 'PAID'
  createdAt: string
  paidAt?: string
}

export interface SettlementUpdateRequest {
  status: 'PENDING' | 'PAID'
  paidAt?: string
}

// API Methods
export const settlementApi = {
  getSettlements: async (): Promise<Settlement[]> => {
    const response = await settlementClient.get<Settlement[]>('')
    return response.data
  },

  getSettlementsByUserId: async (userId: string): Promise<Settlement[]> => {
    const response = await settlementClient.get<Settlement[]>('', {
      params: { userId }
    })
    return response.data
  },

  markSettlementAsPaid: async (settlementId: number): Promise<Settlement> => {
    const response = await settlementClient.put<Settlement>(`/${settlementId}`, {
      status: 'PAID',
      paidAt: new Date().toISOString()
    })
    return response.data
  },

  updateSettlement: async (settlementId: number, data: SettlementUpdateRequest): Promise<Settlement> => {
    const response = await settlementClient.put<Settlement>(`/${settlementId}`, data)
    return response.data
  }
}

export default settlementApi
