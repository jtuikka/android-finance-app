package com.jeture.budget.ui.screens.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeture.budget.domain.model.Transaction
import com.jeture.budget.domain.model.TransactionType
import com.jeture.budget.domain.repo.BudgetRepository
import com.jeture.budget.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val addTx: AddTransactionUseCase,
    repo: BudgetRepository
) : ViewModel() {

    val categories = repo.observeCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _state = MutableStateFlow(AddEditState())
    val state: StateFlow<AddEditState> = _state.asStateFlow()

    fun setAmount(text: String) = _state.update { it.copy(amountText = text, error = null) }
    fun setType(type: TransactionType) = _state.update { it.copy(type = type, error = null) }
    fun setCategory(id: Long) = _state.update { it.copy(categoryId = id, error = null) }
    fun setDate(date: LocalDate) = _state.update { it.copy(date = date, error = null) }
    fun setNote(note: String) = _state.update { it.copy(note = note, error = null) }

    fun save() = viewModelScope.launch {
        val s = _state.value
        val catId = s.categoryId ?: run {
            _state.update { it.copy(error = "Select a category") }
            return@launch
        }
        val amountCents = parseToCentsOrNull(s.amountText) ?: run {
            _state.update { it.copy(error = "Invalid amount") }
            return@launch
        }

        _state.update { it.copy(isSaving = true, error = null) }

        val tx = Transaction(
            amountCents = amountCents,
            type = s.type,
            categoryId = catId,
            date = s.date,
            note = s.note.ifBlank { null }
        )

        val res = addTx(tx)
        _state.update {
            if (res.isSuccess) it.copy(isSaving = false, saved = true)
            else it.copy(isSaving = false, error = res.exceptionOrNull()?.message ?: "Save failed")
        }
    }

    private fun parseToCentsOrNull(text: String): Long? {
        val cleaned = text.replace(",", ".").trim()
        val value = cleaned.toDoubleOrNull() ?: return null
        if (value <= 0) return null
        return (value * 100.0).toLong()
    }
}
