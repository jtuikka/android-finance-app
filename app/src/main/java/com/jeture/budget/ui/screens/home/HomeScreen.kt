package com.jeture.budget.ui.screens.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import com.jeture.budget.domain.model.Category
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign


data class PieSlice(
    val label: String,
    val value: Float,
    val color: Color
)


private fun formatEur(cents: Long): String = "€%.2f".format(cents / 100.0)

private fun arrowFor(p: Double?): String = when {
    p == null -> ""
    p > 0 -> " ↑"
    p < 0 -> " ↓"
    else -> ""
}

private fun formatPct(p: Double?): String = p?.let { "%+.0f%%%s".format(it, arrowFor(it)) } ?: "—"

private fun topCategoriesToPie(top: List<Pair<Category, Long>>): List<PieSlice> {
    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFFE91E63),
        Color(0xFF9C27B0)
    )

    return top.mapIndexed { i, (cat, cents) ->
        PieSlice(
            label = cat.name,
            value = cents / 100f,
            color = colors[i % colors.size]
        )
    }
}

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

@Composable
fun ExpenseDonutChart(
    slices: List<PieSlice>,
    totalText: String,
    modifier: Modifier = Modifier.size(220.dp)
) {
    val total = slices.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(1f)

    val progress by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(900, easing = FastOutSlowInEasing),
        label = "donutAnim"
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                var startAngle = -90f

                slices.forEach { slice ->
                    val sweep = (slice.value / total) * 360f * progress

                    drawArc(
                        color = slice.color,
                        startAngle = startAngle,
                        sweepAngle = sweep,
                        useCenter = false,
                        style = Stroke(width = 40f)
                    )

                    startAngle += (slice.value / total) * 360f
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = totalText,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            slices.forEach { slice ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .background(slice.color, shape = CircleShape)
                    )

                    Text(
                        text = "${slice.label}: €%.0f".format(slice.value),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    TextButton(onClick = vm::prevMonth) { Text("Prev") }
                    TextButton(onClick = vm::nextMonth) { Text("Next") }
                }
            )
        },
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
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
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Button(onClick = { onAdd(s.month.yearMonth.toString()) }) {
                            Text("Add transaction")
                        }
                    }

                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Top expense categories",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center)
                    if (s.topCategories.isEmpty()) Text("No expenses yet this month.")
                    else s.topCategories.forEach { (cat, cents) ->
                        Text("• ${cat.name}: ${formatEur(cents)}")
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Expense distribution",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(12.dp))

                    if (s.topCategories.isEmpty()) {
                        Text("No expenses yet this month.")
                    } else {
                        ExpenseDonutChart(
                            slices = topCategoriesToPie(s.topCategories),
                            totalText = formatEur(s.expenseCents)
                        )
                    }
                }
            }
        }
    }
}
