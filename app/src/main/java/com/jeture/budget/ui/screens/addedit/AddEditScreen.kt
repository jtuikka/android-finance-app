package com.jeture.budget.ui.screens.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeture.budget.domain.model.TransactionType
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(initialYearMonth: String, onDone: () -> Unit, vm: AddEditViewModel = hiltViewModel()) {
    val s by vm.state.collectAsState()
    val categories by vm.categories.collectAsState()

    LaunchedEffect(s.saved) { if (s.saved) onDone() }

    LaunchedEffect(initialYearMonth) {
        val ym = java.time.YearMonth.parse(initialYearMonth)
        vm.setDate(ym.atDay(1))
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Add transaction") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = s.amountText,
                onValueChange = vm::setAmount,
                label = { Text("Amount (EUR)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = s.type == TransactionType.EXPENSE,
                    onClick = { vm.setType(TransactionType.EXPENSE) },
                    label = { Text("Expense") }
                )
                FilterChip(
                    selected = s.type == TransactionType.INCOME,
                    onClick = { vm.setType(TransactionType.INCOME) },
                    label = { Text("Income") }
                )
            }

            Text("Category", style = MaterialTheme.typography.titleMedium)

            categories.forEach { c ->
                Row(
                    Modifier.fillMaxWidth()
                        .clickable { vm.setCategory(c.id) }
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RadioButton(
                        selected = s.categoryId == c.id,
                        onClick = { vm.setCategory(c.id) }
                    )
                    Text(c.name)
                }
            }

            OutlinedTextField(
                value = s.note,
                onValueChange = vm::setNote,
                label = { Text("Note (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            s.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            Button(
                onClick = vm::save,
                enabled = !s.isSaving,
                modifier = Modifier.fillMaxWidth()
            ) { Text(if (s.isSaving) "Saving..." else "Save") }
        }
    }
}
