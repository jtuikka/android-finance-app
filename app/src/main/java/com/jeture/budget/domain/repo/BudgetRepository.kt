package com.jeture.budget.domain.repo

import com.jeture.budget.data.dao.CategorySpendRow
import com.jeture.budget.data.dao.MonthlyTotalsRow
import com.jeture.budget.domain.model.Category
import com.jeture.budget.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun observeCategories(): Flow<List<Category>>
    fun observeMonthlyTotals(fromEpochDay: Long, toEpochDay: Long): Flow<MonthlyTotalsRow>
    fun observeTopExpenseCategories(fromEpochDay: Long, toEpochDay: Long, limit: Int): Flow<List<CategorySpendRow>>

    suspend fun upsertTransaction(tx: Transaction): Long
    suspend fun seedCategoriesIfEmpty()
}
