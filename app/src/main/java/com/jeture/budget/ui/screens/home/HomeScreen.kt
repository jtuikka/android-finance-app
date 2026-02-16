package com.jeture.budget.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme



private fun formatEur(cents: Long): String = "€%.2f".format(cents / 100.0)

private fun arrowFor(p: Double?): String = when {
    p == null -> ""
    p > 0 -> " ↑"
    p < 0 -> " ↓"
    else -> ""
}

private fun formatPct(p: Double?): String = p?.let { "%+.0f%%%s".format(it, arrowFor(it)) } ?: "—"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun percentColor(p: Double?, positiveIsGood: Boolean): Color {
    if (p == null) return MaterialTheme.colorScheme.onSurfaceVariant

    val good = if (positiveIsGood) p > 0 else p < 0

    return if (good)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.error
}

@Composable
private fun SummaryRow(label: String, amountText: String, pct: Double?, positiveIsGood: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label $amountText")
        Text(
            text = formatPct(pct),
            color = percentColor(pct, positiveIsGood)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAdd: (String) -> Unit,
    vm: HomeViewModel = hiltViewModel()
) {
    val s by vm.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${s.month.yearMonth}") },
                actions = {
                    TextButton(onClick = vm::prevMonth) { Text("Prev") }
                    TextButton(onClick = vm::nextMonth) { Text("Next") }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAdd(s.month.yearMonth.toString()) }
            ) {
                Text("+")
            }
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    SummaryRow(
                        label = "Income:",
                        amountText = formatEur(s.incomeCents),
                        pct = s.incomeChangePct,
                        positiveIsGood = true
                    )

                    SummaryRow(
                        label = "Expense:",
                        amountText = formatEur(s.expenseCents),
                        pct = s.expenseChangePct,
                        positiveIsGood = false
                    )

                    Divider()

                    SummaryRow(
                        label = "Net:",
                        amountText = formatEur(s.netCents),
                        pct = s.netChangePct,
                        positiveIsGood = true,
                    )
                }
            }

            Card {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Top expense categories", style = MaterialTheme.typography.titleMedium)
                    if (s.topCategories.isEmpty()) Text("No expenses yet this month.")
                    else s.topCategories.forEach { (cat, cents) ->
                        Text("• ${cat.name}: ${formatEur(cents)}")
                    }
                }
            }
        }
    }
}
