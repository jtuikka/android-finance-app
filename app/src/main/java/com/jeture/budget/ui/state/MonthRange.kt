package com.jeture.budget.ui.state

import java.time.LocalDate
import java.time.YearMonth

data class MonthRange(val yearMonth: YearMonth) {
    val from: LocalDate = yearMonth.atDay(1)
    val to: LocalDate = yearMonth.atEndOfMonth()
    val fromEpochDay: Long = from.toEpochDay()
    val toEpochDay: Long = to.toEpochDay()
}
