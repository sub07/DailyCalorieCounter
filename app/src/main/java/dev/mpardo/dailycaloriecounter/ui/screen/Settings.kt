package dev.mpardo.dailycaloriecounter.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.ui.component.NumberInput
import dev.mpardo.dailycaloriecounter.ui.component.NumberInputValue
import dev.mpardo.dailycaloriecounter.ui.component.TextInputSelection

@Composable
fun SettingsScreen(
    dailyCalorieGoal: Int,
    dailyProteinGoal: Int,
    onCalorieGoalUpdate: (Int) -> Unit,
    onProteinGoalUpdate: (Int) -> Unit,
) {
    var showDailyCalorieGoalDialog by remember { mutableStateOf(false) }
    var showDailyProteinGoalDialog by remember { mutableStateOf(false) }
    
    Column {
        SettingsItem {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.calorie_goal_label), modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                        .fillMaxWidth(0.6f)
                )
                TextButton({ showDailyCalorieGoalDialog = true }, modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("$dailyCalorieGoal kcal")
                }
            }
        }
        
        SettingsItem {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    stringResource(R.string.protein_goal_label), modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                        .fillMaxWidth(0.6f)
                )
                TextButton({ showDailyProteinGoalDialog = true }, modifier = Modifier.align(Alignment.CenterVertically)) {
                    Text("$dailyProteinGoal g")
                }
            }
        }
    }
    
    if (showDailyCalorieGoalDialog) {
        EditDailyGoalDialog(
            dailyCalorieGoal,
            onDismiss = { showDailyCalorieGoalDialog = false },
            onEdit = {
                onCalorieGoalUpdate(it)
                showDailyCalorieGoalDialog = false
            },
            label = stringResource(R.string.calorie_goal_label),
        )
    }
    
    if (showDailyProteinGoalDialog) {
        EditDailyGoalDialog(
            dailyProteinGoal,
            onDismiss = { showDailyProteinGoalDialog = false },
            onEdit = {
                onProteinGoalUpdate(it)
                showDailyProteinGoalDialog = false
            },
            label = stringResource(R.string.protein_goal_label),
        )
    }
}

@Composable
fun SettingsItem(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        content()
    }
}

@Composable
fun EditDailyGoalDialog(
    goal: Int,
    label: String,
    onDismiss: () -> Unit = {},
    onEdit: (Int) -> Unit = {}
) {
    var newGoal by remember { mutableStateOf(NumberInputValue(goal)) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                NumberInput(
                    value = newGoal,
                    onValueChange = { newGoal = it },
                    onSubmit = { onEdit(it.toInt()) },
                    label = { Text(label) },
                    requestFocus = true,
                    initialSelection = TextInputSelection.Full,
                    keyboardIcon = ImeAction.Done
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismiss) { Text(stringResource(R.string.cancel), color = contentColorFor(MaterialTheme.colors.background)) }
                    TextButton({ onEdit(newGoal.value.toInt()) }) { Text(stringResource(R.string.edit)) }
                }
            }
        }
    }
}