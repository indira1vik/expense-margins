<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
	<main class="mx-auto max-w-6xl px-4 py-8 sm:px-6 lg:px-8">
		<div class="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
			<div class="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
				<div>
					<p class="text-sm font-medium uppercase tracking-[0.2em] text-slate-500">Settlements</p>
					<h1 class="mt-3 text-3xl font-bold text-slate-900">Pending settlements</h1>
					<p class="mt-2 text-slate-600">Settlement list integration is pending backend endpoint availability.</p>
				</div>
				<div class="flex flex-wrap gap-3">
					<RouterLink
						to="/dashboard"
						class="rounded-full bg-slate-900 px-5 py-3 text-sm font-semibold text-white transition hover:bg-slate-700"
					>
						Back to Dashboard
					</RouterLink>
					<RouterLink
						to="/expense"
						class="rounded-full border border-slate-200 px-5 py-3 text-sm font-semibold text-slate-700 transition hover:bg-slate-50"
					>
						View Expenses
					</RouterLink>
				</div>
			</div>
			<div class="mt-8 rounded-2xl border border-slate-200 bg-slate-50 p-6 text-sm text-slate-600">
				No settlement data is being filled with demo values anymore.
			</div>
		</div>
	</main>
</template>
