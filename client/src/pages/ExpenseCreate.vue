<script setup lang="ts">
import { RouterLink } from 'vue-router'
import { ref } from 'vue'
import expenseApi from '@/api/expense.api'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

const formData = ref({
  title: '',
  totalAmount: '',
  currency: 'USD',
  splitType: 'EQUAL'
})

const submitError = ref('')
const submitSuccess = ref('')
const isSubmitting = ref(false)

const handleSubmit = async () => {
	submitError.value = ''
	submitSuccess.value = ''

	if (!authStore.userId) {
		submitError.value = 'You must be logged in to create an expense.'
		return
	}

	isSubmitting.value = true
	try {
		await expenseApi.createExpense({
			title: formData.value.title,
			totalAmount: Number(formData.value.totalAmount),
			currency: formData.value.currency,
			createdByUserId: authStore.userId,
			splitType: formData.value.splitType as 'EQUAL' | 'EXACT' | 'PERCENT',
			splits: [
				{
					userId: authStore.userId,
					amount: Number(formData.value.totalAmount),
					percentage: 100
				}
			]
		})

		submitSuccess.value = 'Expense created successfully.'
		formData.value = {
			title: '',
			totalAmount: '',
			currency: 'USD',
			splitType: 'EQUAL'
		}
	} catch (error: any) {
		submitError.value = error?.response?.data?.message || 'Failed to create expense.'
	} finally {
		isSubmitting.value = false
	}
}
</script>

<template>
	<main class="mx-auto max-w-4xl px-4 py-8 sm:px-6 lg:px-8">
		<div class="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
			<div class="flex items-center justify-between gap-4">
				<div>
					<p class="text-sm font-medium uppercase tracking-[0.2em] text-slate-500">Create Expense</p>
					<h1 class="mt-3 text-3xl font-bold text-slate-900">Create a new expense</h1>
					<p class="mt-2 text-slate-600">This form submits to backend.</p>
				</div>
				<RouterLink
					to="/dashboard"
					class="rounded-full border border-slate-200 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
				>
					Back to Dashboard
				</RouterLink>
			</div>

			<form class="mt-8 space-y-5" @submit.prevent="handleSubmit">
				<div>
					<label class="mb-2 block text-sm font-medium text-slate-700">Title</label>
					<input v-model="formData.title" type="text" class="w-full rounded-2xl border border-slate-200 px-4 py-3 outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100" placeholder="Dinner, trip, snacks..." />
				</div>
				<div class="grid gap-4 sm:grid-cols-2">
					<div>
						<label class="mb-2 block text-sm font-medium text-slate-700">Total Amount</label>
						<input v-model="formData.totalAmount" type="number" min="0.01" step="0.01" class="w-full rounded-2xl border border-slate-200 px-4 py-3 outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100" placeholder="84.00" />
					</div>
					<div>
						<label class="mb-2 block text-sm font-medium text-slate-700">Currency</label>
						<select v-model="formData.currency" class="w-full rounded-2xl border border-slate-200 px-4 py-3 outline-none focus:border-indigo-500 focus:ring-4 focus:ring-indigo-100">
							<option>USD</option>
							<option>EUR</option>
							<option>INR</option>
						</select>
					</div>
				</div>
				<div>
					<label class="mb-2 block text-sm font-medium text-slate-700">Split Type</label>
					<div class="grid gap-3 sm:grid-cols-3">
						<label v-for="type in ['EQUAL', 'EXACT', 'PERCENT']" :key="type" class="cursor-pointer rounded-2xl border px-4 py-3 text-center text-sm font-semibold transition" :class="formData.splitType === type ? 'border-indigo-600 bg-indigo-50 text-indigo-700' : 'border-slate-200 text-slate-600'">
							<input v-model="formData.splitType" :value="type" type="radio" class="sr-only" />
							{{ type }}
						</label>
					</div>
				</div>
				<button :disabled="isSubmitting" type="submit" class="rounded-full bg-indigo-600 px-6 py-3 font-semibold text-white transition hover:bg-indigo-700 disabled:bg-slate-400">
					{{ isSubmitting ? 'Saving...' : 'Create Expense' }}
				</button>
			</form>

			<p v-if="submitSuccess" class="mt-5 rounded-2xl bg-emerald-50 px-4 py-3 text-sm text-emerald-700">
				{{ submitSuccess }}
			</p>
			<p v-if="submitError" class="mt-5 rounded-2xl bg-red-50 px-4 py-3 text-sm text-red-700">
				{{ submitError }}
			</p>
		</div>
	</main>
</template>
