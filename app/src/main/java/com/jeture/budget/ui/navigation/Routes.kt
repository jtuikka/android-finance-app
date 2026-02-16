package com.jeture.budget.ui.navigation

object Routes {
    const val HOME = "home"
    const val ADD = "add/{ym}"

    fun add(ym: String) = "add/$ym"
}
