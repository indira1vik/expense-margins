<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import expenseApi, { type Expense } from '@/api/expense.api'

const expenses = ref<Expense[]>([])
const isLoading = ref(false)
const loadError = ref('')

const sortedExpenses = computed(() => [...expenses.value].sort((a, b) => b.id - a.id))

const loadExpenses = async () => {
	isLoading.value = true
	loadError.value = ''
	try {
		expenses.value = await expenseApi.getExpenses()
	} catch (error: any) {
		loadError.value = error?.response?.data?.message || 'Unable to load expenses.'
	} finally {
		isLoading.value = false
	}
}

onMounted(loadExpenses)
</script>

<template>
	<main class="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8">
		<div class="mb-6 flex items-end justify-between gap-4">
			<div>
				<p class="text-sm font-medium uppercase tracking-[0.2em] text-slate-500">Expenses</p>
				<h1 class="mt-2 text-3xl font-bold text-slate-900">All expenses</h1>
				<p class="mt-2 text-slate-600">Live data from backend.</p>
			</div>
			<RouterLink
				to="/expense/create"
				class="rounded-full bg-indigo-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-indigo-700"
			>
				Create Expense
			</RouterLink>
		</div>

		<p v-if="loadError" class="mb-4 rounded-xl bg-red-50 px-3 py-2 text-sm text-red-700">
			{{ loadError }}
		</p>

		<p v-if="isLoading" class="text-sm text-slate-500">Loading expenses...</p>

		<p v-else-if="sortedExpenses.length === 0" class="rounded-2xl border border-slate-200 bg-white p-6 text-sm text-slate-500">
			No expenses found yet.
		</p>

		<div v-else class="grid gap-4">
			<article
				v-for="expense in sortedExpenses"
				:key="expense.id"
				class="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm"
			>
				<div class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
					<div>
						<div class="flex items-center gap-3">
							<h2 class="text-lg font-semibold text-slate-900">{{ expense.title }}</h2>
							<span class="inline-flex rounded-full bg-slate-100 px-3 py-1 text-xs font-medium text-slate-700">
								{{ expense.type || 'N/A' }}
							</span>
						</div>
						<p class="mt-2 text-sm text-slate-500">
							{{ expense.createdByUserId }} · {{ expense.createdAt }}
						</p>
					</div>
					<div class="flex items-center gap-4">
						<div class="text-right">
							<div class="text-xl font-bold text-slate-900">${{ expense.totalAmount.toFixed(2) }}</div>
							<div class="text-sm text-slate-500">{{ expense.currency }}</div>
						</div>
						<RouterLink
							:to="`/expense/${expense.id}`"
							class="rounded-full border border-slate-200 px-4 py-2 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
						>
							View
						</RouterLink>
					</div>
				</div>
			</article>
		</div>
	</main>
</template>
