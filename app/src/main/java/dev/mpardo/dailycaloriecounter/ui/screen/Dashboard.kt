package dev.mpardo.dailycaloriecounter.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import dev.mpardo.dailycaloriecounter.ui.component.*

@Composable
fun DashboardScreen(
    dailyCalorieGoal: Int,
    totalCalorieRecorded: Int,
    foodEntries: List<FoodEntry>,
    foodEntryUses: Map<FoodEntry, Int>,
    onAddFoodUse: (FoodEntry) -> Unit,
    onAddRecord: (FoodRecord) -> Unit,
    onClearAllRecord: () -> Unit,
) {
    var showAddFoodDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Card(modifier = Modifier.padding(bottom = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(stringResource(R.string.dashboard_left), style = MaterialTheme.typography.h6, modifier = Modifier.padding(bottom = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgress(
                        progress = totalCalorieRecorded / dailyCalorieGoal.toFloat(),
                        size = 128.dp,
                        text = stringResource(R.string.dashboard_kcal_left, (dailyCalorieGoal - totalCalorieRecorded).coerceAtLeast(0), "\n"),
                        barColor = if (totalCalorieRecorded > dailyCalorieGoal) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                        textColor = if (totalCalorieRecorded > dailyCalorieGoal) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                    )
                }
                Text(
                    text = stringResource(R.string.dashboard_consumed_kcal, totalCalorieRecorded, "\n"),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 8.dp)
                )
            }
        }
        
        Button(
            onClick = { onClearAllRecord() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.error)
        ) {
            Text(stringResource(R.string.clear_records))
        }
    }
    
    Column(
        verticalArrangement = Arrangement.Bottom, modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            FloatingActionButton(onClick = { showAddFoodDialog = true }) {
                Icon(Icons.Filled.Add, "Add food")
            }
        }
    }
    
    if (showAddFoodDialog) {
        AddFoodRecordDialog(
            onAddRecord = {
                showAddFoodDialog = false
                onAddRecord(it)
                onAddFoodUse(it.food)
            },
            onDismiss = { showAddFoodDialog = false },
            foodEntries,
            foodEntryUses,
        )
    }
}

@Composable
fun AddFoodRecordDialog(
    onAddRecord: (FoodRecord) -> Unit,
    onDismiss: () -> Unit,
    foodEntries: List<FoodEntry>,
    foodEntryUses: Map<FoodEntry, Int>,
) {
    var mass by remember { mutableStateOf(NumberInputValue(0)) }
    var food: FoodEntry? by remember { mutableStateOf(null) }
    var search by remember { mutableStateOf(TextFieldValue("")) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(elevation = 8.dp, shape = RoundedCornerShape(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center
            ) {
                Column {
                    TextInput(
                        value = search,
                        onValueChange = { search = it },
                        isError = food == null,
                        enabled = food == null,
                        label = { Text(stringResource(R.string.dashboard_dialog_food_label)) },
                        placeholder = { Text(stringResource(R.string.dashboard_dialog_food_placeholder)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                food = null
                                search = search.copy(text = "")
                            },
                    )
                    if (food == null) {
                        Card(elevation = 16.dp, modifier = Modifier.fillMaxWidth()) {
                            LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                                items(foodEntries.filter { it.name.startsWith(search.text, true) }.sortedByDescending { foodEntryUses[it] }) {
                                    Text(
                                        it.name,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 16.dp)
                                            .clickable {
                                                food = it
                                                search = search.copy(text = it.name)
                                            },
                                        color = MaterialTheme.colors.primary,
                                    )
                                }
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    NumberInput(
                        value = mass,
                        onValueChange = { mass = it },
                        label = { Text(stringResource(R.string.dashboard_dialog_mass_label)) },
                        modifier = Modifier.fillMaxWidth(),
                        requestFocus = true,
                        initialSelection = TextInputSelection.Full,
                        onSubmit = {
                            food?.let {
                                onAddRecord(FoodRecord(-1, it, mass.value.toInt()))
                            }
                        },
                        keyboardIcon = ImeAction.Done,
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(modifier = Modifier.align(Alignment.End)) {
                        TextButton(onDismiss) { Text(stringResource(R.string.cancel), color = contentColorFor(MaterialTheme.colors.background)) }
                        TextButton(enabled = food != null, onClick = {
                            food?.let {
                                onAddRecord(FoodRecord(-1, food!!, mass.value.toInt()))
                                
                            }
                        }) { Text(stringResource(R.string.add)) }
                    }
                }
            }
        }
    }
}