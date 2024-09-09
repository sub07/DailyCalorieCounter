package me.mpardo.dailycaloriecounter.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.mpardo.dailycaloriecounter.R
import me.mpardo.dailycaloriecounter.model.Carbohydrates
import me.mpardo.dailycaloriecounter.model.Energy
import me.mpardo.dailycaloriecounter.model.Fats
import me.mpardo.dailycaloriecounter.model.Proteins
import me.mpardo.dailycaloriecounter.model.Salt
import me.mpardo.dailycaloriecounter.ui.component.NumberInput
import me.mpardo.dailycaloriecounter.ui.component.NumberInputValue
import me.mpardo.dailycaloriecounter.ui.component.TextInputSelection

@Composable
fun SettingsScreen(
    dailyCalorieGoal: Int,
    dailyProteinGoal: Int,
    dailyFatsGoal: Int,
    dailyCarbohydratesGoal: Int,
    dailySaltGoal: Int,
    onCalorieGoalUpdate: (Energy) -> Unit,
    onProteinGoalUpdate: (Proteins) -> Unit,
    onFatsGoalUpdate: (Fats) -> Unit,
    onCarbohydratesGoalUpdate: (Carbohydrates) -> Unit,
    onSaltGoalUpdate: (Salt) -> Unit,
    onAddCustomCalorieAmount: (Int) -> Unit,
) {
    var showDailyCalorieGoalDialog by remember { mutableStateOf(false) }
    var showDailyProteinGoalDialog by remember { mutableStateOf(false) }
    var showDailyFatsGoalDialog by remember { mutableStateOf(false) }
    var showDailyCarbohydratesGoalDialog by remember { mutableStateOf(false) }
    var showDailySaltGoalDialog by remember { mutableStateOf(false) }

    Column {
        GoalItem(name = stringResource(R.string.calorie_goal_label),
            value = dailyCalorieGoal,
            suffix = "kcal",
            onGoalClick = { showDailyCalorieGoalDialog = true })

        GoalItem(name = stringResource(R.string.protein_goal_label),
            value = dailyProteinGoal,
            suffix = "g",
            onGoalClick = { showDailyProteinGoalDialog = true })

        GoalItem(name = stringResource(R.string.fats_goal_label),
            value = dailyFatsGoal,
            suffix = "g",
            onGoalClick = { showDailyFatsGoalDialog = true })

        GoalItem(name = stringResource(R.string.carbohydrates_goal_label),
            value = dailyCarbohydratesGoal,
            suffix = "g",
            onGoalClick = { showDailyCarbohydratesGoalDialog = true })

        GoalItem(name = stringResource(R.string.salt_goal_label),
            value = dailySaltGoal,
            suffix = "g",
            onGoalClick = { showDailySaltGoalDialog = true })
    }

    if (showDailyCalorieGoalDialog) {
        SimpleNumberDialog(
            dailyCalorieGoal,
            onDismiss = { showDailyCalorieGoalDialog = false },
            onValueSubmit = {
                onCalorieGoalUpdate(Energy(it.toFloat()))
                showDailyCalorieGoalDialog = false
            },
            label = stringResource(R.string.calorie_goal_label),
        )
    }

    if (showDailyProteinGoalDialog) {
        SimpleNumberDialog(
            dailyProteinGoal,
            onDismiss = { showDailyProteinGoalDialog = false },
            onValueSubmit = {
                onProteinGoalUpdate(Proteins(it.toFloat()))
                showDailyProteinGoalDialog = false
            },
            label = stringResource(R.string.protein_goal_label),
        )
    }

    if (showDailyFatsGoalDialog) {
        SimpleNumberDialog(
            dailyFatsGoal,
            onDismiss = { showDailyFatsGoalDialog = false },
            onValueSubmit = {
                onFatsGoalUpdate(Fats(it.toFloat()))
                showDailyFatsGoalDialog = false
            },
            label = stringResource(R.string.fats_goal_label),
        )
    }

    if (showDailyCarbohydratesGoalDialog) {
        SimpleNumberDialog(
            dailyCarbohydratesGoal,
            onDismiss = { showDailyCarbohydratesGoalDialog = false },
            onValueSubmit = {
                onCarbohydratesGoalUpdate(Carbohydrates(it.toFloat()))
                showDailyCarbohydratesGoalDialog = false
            },
            label = stringResource(R.string.carbohydrates_goal_label),
        )
    }

    if (showDailySaltGoalDialog) {
        SimpleNumberDialog(
            dailySaltGoal,
            onDismiss = { showDailySaltGoalDialog = false },
            onValueSubmit = {
                onSaltGoalUpdate(Salt(it.toFloat()))
                showDailySaltGoalDialog = false
            },
            label = stringResource(R.string.salt_goal_label),
        )
    }

    var showCustomEnergyDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Bottom, modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = { showCustomEnergyDialog = true }) {
                Text(text = stringResource(R.string.add_custom_calorie_amount_label))
            }
        }
    }

    if (showCustomEnergyDialog) {
        SimpleNumberDialog(
            value = 0,
            label = stringResource(R.string.energy_name),
            onDismiss = { showCustomEnergyDialog = false },
            onValueSubmit = {
                showCustomEnergyDialog = false
                onAddCustomCalorieAmount(it)
            })
    }
}

@Composable
fun GoalItem(name: String, value: Int, suffix: String, onGoalClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                name, modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .fillMaxWidth(0.6f)
            )
            TextButton(
                onGoalClick,
                modifier = Modifier.align(Alignment.CenterVertically),
            ) {
                Text("$value$suffix", textAlign = TextAlign.End)
            }
        }
    }
}

@Composable
fun SimpleNumberDialog(
    value: Number,
    label: String,
    onDismiss: () -> Unit = {},
    onValueSubmit: (Int) -> Unit = {}
) {
    var newGoal by remember { mutableStateOf(NumberInputValue(value)) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                NumberInput(
                    value = newGoal,
                    onValueChange = { newGoal = it },
                    onSubmit = { onValueSubmit(it.toInt()) },
                    label = { Text(label) },
                    focused = true,
                    initialSelection = TextInputSelection.Full,
                    keyboardIcon = ImeAction.Done
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismiss) {
                        Text(
                            stringResource(R.string.cancel),
                            color = contentColorFor(MaterialTheme.colors.background)
                        )
                    }
                    TextButton({ onValueSubmit(newGoal.value.toInt()) }) { Text(stringResource(R.string.submit)) }
                }
            }
        }
    }
}