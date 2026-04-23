<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import { useRoute } from 'vue-router'
import expenseApi, { type Expense } from '@/api/expense.api'

const route = useRoute()
const expense = ref<Expense | null>(null)
const isLoading = ref(false)
const loadError = ref('')

const loadExpense = async () => {
	isLoading.value = true
	loadError.value = ''
	try {
		const id = Number(route.params.id)
		expense.value = await expenseApi.getExpenseById(id)
	} catch (error: any) {
		loadError.value = error?.response?.data?.message || 'Unable to load expense details.'
	} finally {
		isLoading.value = false
	}
}

onMounted(loadExpense)
</script>

<template>
	<main class="mx-auto max-w-5xl px-4 py-8 sm:px-6 lg:px-8">
		<div class="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
			<p v-if="loadError" class="mb-4 rounded-xl bg-red-50 px-3 py-2 text-sm text-red-700">{{ loadError }}</p>
			<p v-if="isLoading" class="text-sm text-slate-500">Loading expense details...</p>

			<template v-else-if="expense">
			<div class="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
				<div>
					<p class="text-sm font-medium uppercase tracking-[0.2em] text-slate-500">Expense Detail</p>
					<h1 class="mt-3 text-3xl font-bold text-slate-900">{{ expense.title }}</h1>
					<p class="mt-2 text-slate-600">Created by {{ expense.createdByUserId }} · {{ expense.createdAt }} · {{ expense.type || 'N/A' }}</p>
				</div>
				<RouterLink
					to="/expense"
					class="rounded-full border border-slate-200 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
				>
					Back to Expenses
				</RouterLink>
			</div>

			<div class="mt-8 grid gap-4 sm:grid-cols-3">
				<div class="rounded-2xl bg-slate-50 p-4">
					<p class="text-sm text-slate-500">Total Amount</p>
					<p class="mt-2 text-2xl font-bold text-slate-900">${{ expense.totalAmount.toFixed(2) }}</p>
				</div>
				<div class="rounded-2xl bg-slate-50 p-4">
					<p class="text-sm text-slate-500">Currency</p>
					<p class="mt-2 text-2xl font-bold text-slate-900">{{ expense.currency }}</p>
				</div>
				<div class="rounded-2xl bg-slate-50 p-4">
					<p class="text-sm text-slate-500">Type</p>
					<p class="mt-2 text-2xl font-bold text-slate-900">{{ expense.type || 'N/A' }}</p>
				</div>
			</div>
			</template>
			<p v-else class="text-sm text-slate-500">Expense not found.</p>
		</div>
	</main>
</template>
