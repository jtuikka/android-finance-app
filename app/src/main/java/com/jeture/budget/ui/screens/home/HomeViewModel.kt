package com.jeture.budget.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeture.budget.domain.model.Category
import com.jeture.budget.domain.repo.BudgetRepository
import com.jeture.budget.domain.usecase.ObserveMonthlyTotalsUseCase
import com.jeture.budget.domain.usecase.ObserveTopExpenseCategoriesUseCase
import com.jeture.budget.domain.usecase.SeedCategoriesIfEmptyUseCase
import com.jeture.budget.ui.state.MonthRange
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val totalsUseCase: ObserveMonthlyTotalsUseCase,
    private val topCatsUseCase: ObserveTopExpenseCategoriesUseCase,
    private val seedCats: SeedCategoriesIfEmptyUseCase,
    private val repo: BudgetRepository
) : ViewModel() {

    private val selectedMonth = MutableStateFlow(MonthRange(YearMonth.now()))

    val state: StateFlow<HomeState> =
        selectedMonth.flatMapLatest { mr ->

            val prevMr = MonthRange(mr.yearMonth.minusMonths(1))

            combine(
                totalsUseCase(mr.fromEpochDay, mr.toEpochDay),
                totalsUseCase(prevMr.fromEpochDay, prevMr.toEpochDay),
                topCatsUseCase(mr.fromEpochDay, mr.toEpochDay, limit = 3),
                repo.observeCategories()
            ) { totalsNow, totalsPrev, topRows, cats ->

                val catMap = cats.associateBy { it.id }

                val top = topRows.mapNotNull { row ->
                    val c: Category = catMap[row.categoryId] ?: return@mapNotNull null
                    c to row.spentCents
                }

                HomeState(
                    month = mr,
                    incomeCents = totalsNow.incomeCents,
                    expenseCents = totalsNow.expenseCents,

                    // 🔹 NEW: previous month totals
                    prevIncomeCents = totalsPrev.incomeCents,
                    prevExpenseCents = totalsPrev.expenseCents,

                    topCategories = top,
                    isLoading = false
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            HomeState(month = selectedMonth.value)
        )

    init { viewModelScope.launch { seedCats() } }

    fun prevMonth() { selectedMonth.update { MonthRange(it.yearMonth.minusMonths(1)) } }
    fun nextMonth() { selectedMonth.update { MonthRange(it.yearMonth.plusMonths(1)) } }
}
