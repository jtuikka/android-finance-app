package com.jeture.budget.domain.usecase

import com.jeture.budget.domain.model.Transaction
import com.jeture.budget.domain.repo.BudgetRepository

class AddTransactionUseCase(private val repo: BudgetRepository) {
    suspend operator fun invoke(tx: Transaction): Result<Long> {
        if (tx.amountCents <= 0) return Result.failure(IllegalArgumentException("Amount must be > 0"))
        return runCatching { repo.upsertTransaction(tx) }
    }
}
