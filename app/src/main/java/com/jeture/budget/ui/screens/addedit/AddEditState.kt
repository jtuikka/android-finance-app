package com.jeture.budget.ui.screens.addedit

import com.jeture.budget.domain.model.TransactionType
import java.time.LocalDate

data class AddEditState(
    val amountText: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long? = null,
    val date: LocalDate = LocalDate.now(),
    val note: String = "",
    val isSaving: Boolean = false,
    val error: String? = null,
    val saved: Boolean = false
)
