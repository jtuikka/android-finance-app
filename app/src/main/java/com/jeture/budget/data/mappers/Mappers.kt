package com.jeture.budget.data.mappers

import com.jeture.budget.data.entity.CategoryEntity
import com.jeture.budget.data.entity.TransactionEntity
import com.jeture.budget.domain.model.Category
import com.jeture.budget.domain.model.Transaction
import java.time.LocalDate

fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    iconKey = iconKey,
    colorKey = colorKey,
    monthlyLimitCents = monthlyLimitCents
)

fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    iconKey = iconKey,
    colorKey = colorKey,
    monthlyLimitCents = monthlyLimitCents
)

fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amountCents = amountCents,
    type = type,
    categoryId = categoryId,
    date = LocalDate.ofEpochDay(dateEpochDay),
    note = note
)

fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amountCents = amountCents,
    type = type,
    categoryId = categoryId,
    dateEpochDay = date.toEpochDay(),
    note = note
)
