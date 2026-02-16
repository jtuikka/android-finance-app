package com.jeture.budget.ui.screens.home

import com.jeture.budget.domain.model.Category
import com.jeture.budget.ui.state.MonthRange

data class HomeState(
    val month: MonthRange,
    val incomeCents: Long = 0,
    val expenseCents: Long = 0,
    val topCategories: List<Pair<Category, Long>> = emptyList(),
    val isLoading: Boolean = true,

    val prevIncomeCents: Long = 0,
    val prevExpenseCents: Long = 0
) {
    val netCents: Long get() = incomeCents - expenseCents
    val prevNetCents: Long get() = prevIncomeCents - prevExpenseCents

    val incomeChangePct: Double? get() = percentChange(prevIncomeCents, incomeCents)
    val expenseChangePct: Double? get() = percentChange(prevExpenseCents, expenseCents)
    val netChangePct: Double? get() = percentChange(prevNetCents, netCents)
}

private fun percentChange(prev: Long, current: Long): Double? {
    if (prev == 0L) return null
    return (current - prev).toDouble() / prev.toDouble() * 100.0
}