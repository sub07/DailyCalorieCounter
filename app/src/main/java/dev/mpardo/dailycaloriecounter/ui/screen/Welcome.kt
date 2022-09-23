package dev.mpardo.dailycaloriecounter.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.ChevronRight
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.ui.component.NumberInput
import dev.mpardo.dailycaloriecounter.ui.component.NumberInputValue
import dev.mpardo.dailycaloriecounter.ui.component.TextInputSelection

@Composable
fun WelcomeScreen(
    onGoalUpdate: (Int) -> Unit,
) {
    var calorieGoal by remember { mutableStateOf(NumberInputValue(2000, TextRange(0, 4))) }
    
    fun submit(goal: Int) {
        onGoalUpdate(goal)
    }
    
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Column {
            Text("Define your calorie goal here :", modifier = Modifier.padding(8.dp))
            NumberInput(
                value = calorieGoal,
                onValueChange = {
                    calorieGoal = it
                },
                keyboardIcon = ImeAction.Done,
                label = { Text(stringResource(R.string.goal_label)) },
                onSubmit = { submit(it.toInt()) },
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                requestFocus = true,
            )
        }
    }
    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(24.dp)) {
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            FloatingActionButton(onClick = { submit(calorieGoal.value.toInt()) }) {
                Icon(FeatherIcons.ChevronRight, contentDescription = "Next Step")
            }
        }
    }
}