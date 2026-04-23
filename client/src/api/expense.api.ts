import axios from 'axios'

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const expenseClient = axios.create({
  baseURL: `${API_BASE_URL}/expenses`,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Interfaces
export type SplitType = 'EQUAL' | 'EXACT' | 'PERCENT'

export interface ExpenseSplit {
  userId: string
  amount?: number // For EXACT split type
  percentage?: number // For PERCENT split type
}

export interface ExpenseCreateRequest {
  title: string
  totalAmount: number
  currency: string
  createdByUserId: string
  splitType: SplitType
  splits: ExpenseSplit[]
}

export interface Expense {
  id: number
  createdByUserId: string
  title: string
  totalAmount: number
  currency: string
  type: string
  createdAt: string
}

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

export interface ExpenseCreateResponse {
  expense: Expense
  settlements: Settlement[]
}

export interface ExpenseDetail {
  expense: Expense
  participants: ExpenseParticipant[]
  settlements: Settlement[]
}

export interface ExpenseParticipant {
  id: number
  expenseId: number
  userId: string
  amountOwed: number
  percentage?: number
  status: string
}

// API Methods
export const expenseApi = {
  createExpense: async (data: ExpenseCreateRequest): Promise<ExpenseCreateResponse> => {
    const response = await expenseClient.post<ExpenseCreateResponse>('', data)
    return response.data
  },

  getExpenses: async (): Promise<Expense[]> => {
    const response = await expenseClient.get<Expense[]>('')
    return response.data
  },

  getExpenseDetail: async (expenseId: number): Promise<ExpenseDetail> => {
    const response = await expenseClient.get<ExpenseDetail>(`/${expenseId}`)
    return response.data
  },

  getExpenseById: async (expenseId: number): Promise<Expense> => {
    const response = await expenseClient.get<Expense>(`/${expenseId}`)
    return response.data
  }
}

export default expenseApi
