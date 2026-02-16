package com.jeture.budget

import com.jeture.budget.domain.model.Transaction
import com.jeture.budget.domain.model.TransactionType
import com.jeture.budget.domain.repo.BudgetRepository
import com.jeture.budget.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

private class FakeRepo : BudgetRepository {
    var last: Transaction? = null

    override fun observeCategories() = flowOf(emptyList<com.jeture.budget.domain.model.Category>())
    override fun observeMonthlyTotals(fromEpochDay: Long, toEpochDay: Long) =
        flowOf(com.jeture.budget.data.dao.MonthlyTotalsRow(0, 0))
    override fun observeTopExpenseCategories(fromEpochDay: Long, toEpochDay: Long, limit: Int) =
        flowOf(emptyList<com.jeture.budget.data.dao.CategorySpendRow>())

    override suspend fun upsertTransaction(tx: Transaction): Long { last = tx; return 1L }
    override suspend fun seedCategoriesIfEmpty() {}
}

class AddTransactionUseCaseTest {
    @Test fun rejectsNonPositiveAmount() = runBlocking {
        val uc = AddTransactionUseCase(FakeRepo())
        val res = uc(Transaction(amountCents = 0, type = TransactionType.EXPENSE, categoryId = 1, date = LocalDate.now()))
        assertTrue(res.isFailure)
    }

    @Test fun acceptsValidAmount() = runBlocking {
        val repo = FakeRepo()
        val uc = AddTransactionUseCase(repo)
        val res = uc(Transaction(amountCents = 1234, type = TransactionType.EXPENSE, categoryId = 1, date = LocalDate.now()))
        assertTrue(res.isSuccess)
        assertNotNull(repo.last)
    }
}
