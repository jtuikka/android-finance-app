package com.jeture.budget.domain.usecase

import com.jeture.budget.data.dao.CategorySpendRow
import com.jeture.budget.data.dao.MonthlyTotalsRow
import com.jeture.budget.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow

class ObserveMonthlyTotalsUseCase(private val repo: BudgetRepository) {
    operator fun invoke(fromEpochDay: Long, toEpochDay: Long): Flow<MonthlyTotalsRow> =
        repo.observeMonthlyTotals(fromEpochDay, toEpochDay)
}

class ObserveTopExpenseCategoriesUseCase(private val repo: BudgetRepository) {
    operator fun invoke(fromEpochDay: Long, toEpochDay: Long, limit: Int = 3): Flow<List<CategorySpendRow>> =
        repo.observeTopExpenseCategories(fromEpochDay, toEpochDay, limit)
}

class SeedCategoriesIfEmptyUseCase(private val repo: BudgetRepository) {
    suspend operator fun invoke() = repo.seedCategoriesIfEmpty()
}
