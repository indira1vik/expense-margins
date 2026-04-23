<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { RouterLink } from 'vue-router'
import expenseApi, { type Expense } from '@/api/expense.api'

const expenses = ref<Expense[]>([])
const isLoading = ref(false)
const loadError = ref('')

const stats = computed(() => [
	{ label: 'Total Expenses', value: String(expenses.value.length), change: 'From backend' },
	{ label: 'Pending Settlements', value: '-', change: 'Not linked yet' },
	{ label: 'You Are Owed', value: '-', change: 'Coming soon' },
	{ label: 'You Owe', value: '-', change: 'Coming soon' }
])

const recentActivity = computed(() => expenses.value.slice(0, 5))

const loadExpenses = async () => {
	isLoading.value = true
	loadError.value = ''
	try {
		expenses.value = await expenseApi.getExpenses()
	} catch (error: any) {
		loadError.value = error?.response?.data?.message || 'Unable to load dashboard data.'
	} finally {
		isLoading.value = false
	}
}

onMounted(loadExpenses)
</script>

<template>
	<main class="mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8">
		<section class="mb-8 rounded-3xl bg-gradient-to-r from-indigo-600 via-blue-600 to-cyan-500 px-6 py-8 text-white shadow-xl shadow-indigo-600/20 sm:px-10">
			<div class="max-w-2xl">
				<p class="text-sm font-medium uppercase tracking-[0.2em] text-white/75">Dashboard</p>
				<h1 class="mt-3 text-3xl font-bold tracking-tight sm:text-4xl">Keep track of shared expenses in one place.</h1>
				<p class="mt-4 max-w-xl text-sm leading-6 text-white/85 sm:text-base">
					See what you owe, what you're owed, and manage every split without losing context.
				</p>
				<div class="mt-6 flex flex-wrap gap-3">
					<RouterLink
						to="/expense/create"
						class="rounded-full bg-white px-5 py-3 text-sm font-semibold text-slate-900 transition hover:bg-slate-100"
					>
						Create Expense
					</RouterLink>
					<RouterLink
						to="/settlement"
						class="rounded-full border border-white/30 px-5 py-3 text-sm font-semibold text-white transition hover:bg-white/10"
					>
						View Settlements
					</RouterLink>
				</div>
			</div>
		</section>

		<section class="mb-8 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
			<div
				v-for="stat in stats"
				:key="stat.label"
				class="rounded-3xl border border-slate-200 bg-white p-5 shadow-sm"
			>
				<p class="text-sm font-medium text-slate-500">{{ stat.label }}</p>
				<div class="mt-3 text-3xl font-bold text-slate-900">{{ stat.value }}</div>
				<p class="mt-2 text-sm text-slate-500">{{ stat.change }}</p>
			</div>
		</section>

		<section class="grid gap-6 lg:grid-cols-[1.6fr_1fr]">
			<div class="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
				<div class="mb-6 flex items-center justify-between">
					<div>
						<h2 class="text-lg font-semibold text-slate-900">Recent Activity</h2>
						<p class="mt-1 text-sm text-slate-500">Latest expenses and split status</p>
					</div>
					<RouterLink to="/expense" class="text-sm font-medium text-indigo-600 hover:text-indigo-700">
						View all
					</RouterLink>
				</div>

				<p v-if="loadError" class="mb-4 rounded-xl bg-red-50 px-3 py-2 text-sm text-red-700">
					{{ loadError }}
				</p>

				<p v-if="isLoading" class="text-sm text-slate-500">Loading recent expenses...</p>

				<p v-else-if="recentActivity.length === 0" class="text-sm text-slate-500">
					No expenses found yet.
				</p>

				<div v-else class="space-y-4">
					<article
						v-for="item in recentActivity"
						:key="item.id"
						class="flex items-center justify-between rounded-2xl bg-slate-50 px-4 py-4"
					>
						<div>
							<h3 class="font-semibold text-slate-900">{{ item.title }}</h3>
							<p class="mt-1 text-sm text-slate-500">Created by {{ item.createdByUserId }} · {{ item.type || 'N/A' }}</p>
						</div>
						<div class="text-right">
							<div class="font-semibold text-slate-900">${{ item.totalAmount.toFixed(2) }}</div>
							<div class="mt-1 text-xs text-slate-500">{{ item.currency }}</div>
						</div>
					</article>
				</div>
			</div>

			<aside class="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
				<h2 class="text-lg font-semibold text-slate-900">Quick Actions</h2>
				<p class="mt-1 text-sm text-slate-500">Start the most common flows faster</p>

				<div class="mt-6 space-y-3">
					<RouterLink
						to="/expense/create"
						class="block rounded-2xl bg-slate-900 px-4 py-4 text-center font-semibold text-white transition hover:bg-slate-700"
					>
						Create new expense
					</RouterLink>
					<RouterLink
						to="/expense"
						class="block rounded-2xl border border-slate-200 px-4 py-4 text-center font-semibold text-slate-700 transition hover:bg-slate-50"
					>
						Browse expenses
					</RouterLink>
					<RouterLink
						to="/settlement"
						class="block rounded-2xl border border-slate-200 px-4 py-4 text-center font-semibold text-slate-700 transition hover:bg-slate-50"
					>
						Review settlements
					</RouterLink>
				</div>
			</aside>
		</section>
	</main>
</template>
