package com.jeture.budget.domain.model

import java.time.LocalDate

enum class TransactionType { INCOME, EXPENSE }

data class Transaction(
    val id: Long = 0L,
    val amountCents: Long,
    val type: TransactionType,
    val categoryId: Long,
    val date: LocalDate,
    val note: String? = null
)

data class Category(
    val id: Long = 0L,
    val name: String,
    val iconKey: String? = null,
    val colorKey: String? = null,
    val monthlyLimitCents: Long? = null
)
