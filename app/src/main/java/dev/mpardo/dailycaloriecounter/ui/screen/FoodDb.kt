package dev.mpardo.dailycaloriecounter.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import dev.mpardo.dailycaloriecounter.ui.component.*

@Composable
fun FoodCalDbListScreen(
    foods: List<FoodEntry>,
    foodRecords: List<FoodRecord>,
    onFoodAdd: (FoodEntry) -> Unit,
    onFoodUpdate: (FoodEntry) -> Unit,
    onFoodDelete: (FoodEntry) -> Unit,
) {
    var showAddFoodDialog by remember { mutableStateOf(false) }
    var editedFood: FoodEntry? by remember { mutableStateOf(null) }
    var showErrorToast: String? by remember { mutableStateOf(null) }
    
    if (foods.isEmpty()) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.food_db_empty), style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center)
                )
            }
        }
    } else {
        Card(modifier = Modifier.padding(8.dp)) {
            DividedLazyColumn(
                list = foods, modifier = Modifier.padding(8.dp)
            ) {
                FoodDbItem(
                    it,
                    onEdit = {
                        showAddFoodDialog = true
                        editedFood = it
                    },
                    onDelete = {
                        if (foodRecords.map { r -> r.food }.contains(it)) {
                            val foodName = it.name.toLowerCase(Locale.current)
                            showErrorToast = "Could not delete $foodName, delete all the records with it first"
                        } else {
                            onFoodDelete(it)
                        }
                    },
                )
            }
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
    
    showErrorToast?.let {
        showErrorToast = null
        SweetError(
            message = it,
            duration = Toast.LENGTH_LONG,
            padding = PaddingValues(bottom = 50.dp),
            contentAlignment = Alignment.BottomCenter,
        )
    }
    
    if (showAddFoodDialog) {
        EditFoodDialog(editedFood, onDismiss = { showAddFoodDialog = false }, onValidate = {
            showAddFoodDialog = false
            onFoodAdd(it)
        }, onEdit = {
            showAddFoodDialog = false
            editedFood = null
            onFoodUpdate(it)
        })
    }
}

@Composable
fun FoodDbItem(item: FoodEntry, onEdit: () -> Unit, onDelete: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
                .fillMaxWidth(0.7f)
        ) {
            Text(
                item.name,
                style = MaterialTheme.typography.body1,
            )
            Text(
                stringResource(R.string.entry_kcal, item.calorieFor100g),
                style = MaterialTheme.typography.caption,
            )
            Text(
                stringResource(R.string.db_protein_100g, item.protein),
                style = MaterialTheme.typography.caption,
            )
        }
        Row {
            IconButton(onClick = onEdit) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Food Entry")
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Food Entry")
            }
        }
    }
}

@Composable
fun EditFoodDialog(
    foodEntry: FoodEntry? = null, onDismiss: () -> Unit = {}, onValidate: (FoodEntry) -> Unit = {}, onEdit: (FoodEntry) -> Unit = {}
) {
    var name by remember { mutableStateOf(TextFieldValue(foodEntry?.name ?: "")) }
    var calorie by remember { mutableStateOf(NumberInputValue(foodEntry?.calorieFor100g ?: 0)) }
    var protein by remember { mutableStateOf(NumberInputValue(foodEntry?.protein ?: 0)) }
    var canConfirm by remember { mutableStateOf(foodEntry?.name?.isNotEmpty() ?: false) }
    var calorieFocused by remember { mutableStateOf(false) }
    var proteinFocused by remember { mutableStateOf(false) }
    
    fun submit() {
        if (foodEntry == null) {
            onValidate(FoodEntry(-1, name.text, calorie.value.toInt(), protein.value.toInt()))
        } else {
            foodEntry.name = name.text
            foodEntry.calorieFor100g = calorie.value.toInt()
            onEdit(foodEntry)
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                TextInput(
                    value = name,
                    onValueChange = {
                        name = it
                        canConfirm = it.text.isNotBlank()
                    },
                    label = { Text(stringResource(R.string.food_db_add_dialog_name_label)) },
                    placeholder = { Text(stringResource(R.string.food_db_add_dialog_name_placeholder)) },
                    isError = !canConfirm,
                    onSubmit = { calorieFocused = true },
                    initialSelection = TextInputSelection.Full,
                    keyboardIcon = ImeAction.Next,
                    requestFocus = true,
                )
                NumberInput(
                    value = calorie,
                    onValueChange = {
                        calorie = it
                        canConfirm = it.value.toInt() >= 0
                    },
                    label = { Text(text = stringResource(R.string.kcal_for_100g)) },
                    isError = !canConfirm,
                    requestFocus = calorieFocused,
                    keyboardIcon = ImeAction.Next,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { proteinFocused = true },
                )
                NumberInput(
                    value = protein,
                    onValueChange = {
                        protein = it
                        canConfirm = it.value.toInt() >= 0
                    },
                    label = { Text(text = stringResource(R.string.protein_100g)) },
                    isError = !canConfirm,
                    requestFocus = proteinFocused,
                    keyboardIcon = ImeAction.Done,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { submit() }
                )
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismiss) { Text(stringResource(R.string.cancel), color = contentColorFor(MaterialTheme.colors.background)) }
                    if (foodEntry == null) {
                        TextButton(onClick = { submit() }, enabled = canConfirm) { Text(stringResource(R.string.create)) }
                    } else {
                        TextButton(onClick = { submit() }, enabled = canConfirm) { Text(stringResource(R.string.edit)) }
                    }
                }
            }
        }
    }
}