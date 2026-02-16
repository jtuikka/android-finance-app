package com.jeture.budget.data.repository

import com.jeture.budget.data.dao.CategoryDao
import com.jeture.budget.data.dao.TransactionDao
import com.jeture.budget.data.entity.CategoryEntity
import com.jeture.budget.data.mappers.toDomain
import com.jeture.budget.data.mappers.toEntity as catToEntity
import com.jeture.budget.data.mappers.toEntity as txToEntity
import com.jeture.budget.domain.model.Category
import com.jeture.budget.domain.model.Transaction
import com.jeture.budget.domain.repo.BudgetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BudgetRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun observeCategories(): Flow<List<Category>> =
        categoryDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeMonthlyTotals(fromEpochDay: Long, toEpochDay: Long) =
        transactionDao.observeMonthlyTotals(fromEpochDay, toEpochDay)

    override fun observeTopExpenseCategories(fromEpochDay: Long, toEpochDay: Long, limit: Int) =
        transactionDao.observeTopExpenseCategories(fromEpochDay, toEpochDay, limit)

    override suspend fun upsertTransaction(tx: Transaction): Long =
        transactionDao.upsert(tx.txToEntity())

    override suspend fun seedCategoriesIfEmpty() {
        if (categoryDao.count() > 0) return
        val seed = listOf(
            CategoryEntity(name = "Food"),
            CategoryEntity(name = "Transport"),
            CategoryEntity(name = "Rent"),
            CategoryEntity(name = "Shopping"),
            CategoryEntity(name = "Salary")
        )
        categoryDao.upsertAll(seed)
    }
}
