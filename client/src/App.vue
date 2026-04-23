<script setup lang="ts">
import { useAuthStore } from '@/stores/auth'
import { RouterLink, RouterView } from 'vue-router'

const authStore = useAuthStore()
</script>

<template>
  <div id="app" class="min-h-screen bg-slate-50 text-slate-900">
    <nav v-if="authStore.isAuthenticated" class="border-b border-slate-200 bg-white/90 backdrop-blur">
      <div class="mx-auto flex max-w-7xl items-center justify-between px-4 py-4 sm:px-6 lg:px-8">
        <div class="text-lg font-semibold tracking-tight text-slate-900">ExpenseMargin</div>
        <div class="flex items-center gap-2 sm:gap-4">
          <RouterLink
            to="/dashboard"
            class="rounded-full px-4 py-2 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
          >
            Dashboard
          </RouterLink>
          <RouterLink
            to="/expense"
            class="rounded-full px-4 py-2 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
          >
            Expenses
          </RouterLink>
          <RouterLink
            to="/settlement"
            class="rounded-full px-4 py-2 text-sm font-medium text-slate-600 transition hover:bg-slate-100 hover:text-slate-900"
          >
            Settlements
          </RouterLink>
          <button
            @click="authStore.logout()"
            class="rounded-full bg-slate-900 px-4 py-2 text-sm font-semibold text-white transition hover:bg-slate-700"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
    <RouterView />
  </div>
</template>
