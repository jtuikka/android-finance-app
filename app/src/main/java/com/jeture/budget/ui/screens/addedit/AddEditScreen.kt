package com.jeture.budget.ui.screens.addedit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeture.budget.domain.model.TransactionType
import java.time.YearMonth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SumInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { new ->
            val cleaned = new
                .replace(',', '.')
                .filterIndexed { i, ch ->
                    ch.isDigit() || (ch == '.' && '.' !in new.substring(0, i))
                }
            onValueChange(cleaned)
        },
        modifier = modifier,
        singleLine = true,
        label = { Text("Sum") },
        supportingText = { Text("Amount") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    initialYearMonth: String,
    onDone: () -> Unit,
    onBack: () -> Unit = onDone,
    vm: AddEditViewModel = hiltViewModel()
) {
    val s by vm.state.collectAsState()
    val categories by vm.categories.collectAsState()

    LaunchedEffect(s.saved) { if (s.saved) onDone() }

    LaunchedEffect(initialYearMonth) {
        val ym = YearMonth.parse(initialYearMonth)
        vm.setDate(ym.atDay(1))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add transactions") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(padding)
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                shape = RoundedCornerShape(16.dp),
                colors =  CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )

            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    TypeTabs(
                        selected = s.type,
                        onSelect = vm::setType,
                        modifier = Modifier.fillMaxWidth()
                    )

                    SumInput(
                        value = s.amountText,
                        onValueChange = vm::setAmount,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("Category", style = MaterialTheme.typography.titleMedium)

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        categories.forEach { c ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { vm.setCategory(c.id) }
                                    .padding(vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = s.categoryId == c.id,
                                    onClick = { vm.setCategory(c.id) }
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(c.name)
                            }
                        }
                    }

                    LabeledInput(
                        value = s.note,
                        onValueChange = vm::setNote,
                        supporting = "Note (optional)",
                        modifier = Modifier.fillMaxWidth()
                    )

                    s.error?.let {
                        Text(it, color = MaterialTheme.colorScheme.error)
                    }

                    Button(
                        onClick = vm::save,
                        enabled = !s.isSaving,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        shape = CircleShape,
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp)
                    ) {
                        Text(if (s.isSaving) "Adding..." else "Add")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TypeTabs(
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        "Income" to TransactionType.INCOME,
        "Expense" to TransactionType.EXPENSE
    )
    val selectedIndex = tabs.indexOfFirst { it.second == selected }.coerceAtLeast(0)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        divider = {},
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex])
            )
        }
    ) {
        tabs.forEachIndexed { index, (label, type) ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onSelect(type) },
                text = { Text(label, textAlign = TextAlign.Center) },
                selectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                unselectedContentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledInput(
    value: String,
    onValueChange: (String) -> Unit,
    supporting: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        label = { Text("Input") },
        supportingText = { Text(supporting) },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear")
                }
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}
