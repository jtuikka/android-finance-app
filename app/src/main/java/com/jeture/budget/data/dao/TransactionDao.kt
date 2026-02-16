package com.jeture.budget.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeture.budget.data.entity.TransactionEntity
import com.jeture.budget.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

data class MonthlyTotalsRow(
    val incomeCents: Long,
    val expenseCents: Long
)

data class CategorySpendRow(
    val categoryId: Long,
    val spentCents: Long
)

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: TransactionEntity): Long

    @Query("""
        SELECT 
            COALESCE(SUM(CASE WHEN type = :income THEN amountCents ELSE 0 END), 0) AS incomeCents,
            COALESCE(SUM(CASE WHEN type = :expense THEN amountCents ELSE 0 END), 0) AS expenseCents
        FROM transactions
        WHERE dateEpochDay BETWEEN :fromEpochDay AND :toEpochDay
    """)
    fun observeMonthlyTotals(
        fromEpochDay: Long,
        toEpochDay: Long,
        income: TransactionType = TransactionType.INCOME,
        expense: TransactionType = TransactionType.EXPENSE
    ): Flow<MonthlyTotalsRow>

    @Query("""
        SELECT categoryId as categoryId,
               COALESCE(SUM(amountCents), 0) as spentCents
        FROM transactions
        WHERE type = :expense
          AND dateEpochDay BETWEEN :fromEpochDay AND :toEpochDay
        GROUP BY categoryId
        ORDER BY spentCents DESC
        LIMIT :limit
    """)
    fun observeTopExpenseCategories(
        fromEpochDay: Long,
        toEpochDay: Long,
        limit: Int = 3,
        expense: TransactionType = TransactionType.EXPENSE
    ): Flow<List<CategorySpendRow>>
}
