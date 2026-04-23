<script setup lang="ts">
import { ref } from 'vue'
import { useRouter, RouterLink } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const formData = ref({
  name: '',
  email: '',
  password: ''
})
const errorMsg = ref('')

const handleSubmit = async () => {
  errorMsg.value = ''
  try {
    await authStore.register(formData.value.name, formData.value.email, formData.value.password)
    router.push('/dashboard')
  } catch (error: any) {
    errorMsg.value = error.response?.data?.message || 'An error occurred'
  }
}
</script>

<template>
  <div class="min-h-screen bg-gradient-to-br from-indigo-50 via-white to-blue-100 flex items-center justify-center px-4 py-12">
    <div class="w-full max-w-md">
      <div class="rounded-3xl bg-white/90 backdrop-blur border border-white/60 shadow-[0_20px_60px_rgba(15,23,42,0.12)] p-8">
        <div class="text-center mb-8">
          <div class="mx-auto mb-4 h-12 w-12 rounded-2xl bg-indigo-600 text-white flex items-center justify-center font-bold text-xl">
            EM
          </div>
          <h1 class="text-3xl font-bold text-slate-900">Create your account</h1>
          <p class="text-slate-500 mt-2">Start splitting expenses with your team</p>
        </div>

        <div v-if="errorMsg" class="mb-6 rounded-2xl border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">
          {{ errorMsg }}
        </div>

        <form @submit.prevent="handleSubmit" class="space-y-4">
          <div>
            <label for="name" class="mb-2 block text-sm font-medium text-slate-700">Full Name</label>
            <input
              id="name"
              v-model="formData.name"
              type="text"
              required
              class="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Enter your full name"
            />
          </div>

          <div>
            <label for="email" class="mb-2 block text-sm font-medium text-slate-700">Email Address</label>
            <input
              id="email"
              v-model="formData.email"
              type="email"
              required
              class="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Enter your email"
            />
          </div>

          <div>
            <label for="password" class="mb-2 block text-sm font-medium text-slate-700">Password</label>
            <input
              id="password"
              v-model="formData.password"
              type="password"
              required
              class="w-full rounded-2xl border border-slate-200 bg-white px-4 py-3 text-slate-900 outline-none transition focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100"
              placeholder="Create a password"
            />
          </div>

          <button
            type="submit"
            :disabled="authStore.isLoading"
            class="mt-2 inline-flex w-full items-center justify-center rounded-2xl bg-indigo-600 px-4 py-3 font-semibold text-white shadow-lg shadow-indigo-600/20 transition hover:bg-indigo-700 disabled:cursor-not-allowed disabled:bg-slate-400"
          >
            {{ authStore.isLoading ? 'Creating account...' : 'Sign Up' }}
          </button>
        </form>

        <p class="mt-6 text-center text-sm text-slate-500">
          Already have an account?
          <RouterLink to="/login" class="font-semibold text-indigo-600 hover:text-indigo-700">
            Sign In
          </RouterLink>
        </p>

      </div>
    </div>
  </div>
</template>
