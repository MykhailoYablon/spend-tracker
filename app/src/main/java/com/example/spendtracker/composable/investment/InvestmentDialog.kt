package com.example.spendtracker.composable.investment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.spendtracker.model.Investment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, Double) -> Unit,
    investment: Investment? = null // null for add, Investment object for edit
) {
    // Pre-populate fields if editing existing investment
    var name by remember { mutableStateOf(investment?.name ?: "") }
    var category by remember { mutableStateOf(investment?.category ?: "") }
    var amount by remember { mutableStateOf(investment?.amount?.toString() ?: "") }

    val investmentCategories =
        listOf("Stocks", "Bonds", "Real Estate", "Cryptocurrency", "Mutual Funds", "Other")
    var expanded by remember { mutableStateOf(false) }

    // Determine if we're adding or editing
    val isEditing = investment != null
    val dialogTitle = if (isEditing) "Edit Investment" else "Add Investment"
    val buttonText = if (isEditing) "Update" else "Add"

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(dialogTitle) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Investment Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        investmentCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isNotBlank() && category.isNotBlank() && amount.isNotBlank()) {
                        onSave(name, category, amount.toDoubleOrNull() ?: 0.0)
                    }
                }
            ) {
                Text(buttonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}