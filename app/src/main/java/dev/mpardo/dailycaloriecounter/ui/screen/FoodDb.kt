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
import com.chargemap.compose.numberpicker.NumberPicker
import com.talhafaki.composablesweettoast.util.SweetToastUtil.SweetError
import dev.mpardo.dailycaloriecounter.R
import dev.mpardo.dailycaloriecounter.model.FoodEntry
import dev.mpardo.dailycaloriecounter.model.FoodRecord
import dev.mpardo.dailycaloriecounter.ui.component.DividedLazyColumn
import dev.mpardo.dailycaloriecounter.ui.component.TextInput
import dev.mpardo.dailycaloriecounter.ui.component.TextInputSelection

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
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(R.string.food_db_empty),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center)
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
    
    Column(verticalArrangement = Arrangement.Bottom, modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth()
        .padding(24.dp)) {
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
        Column(modifier = Modifier
            .align(Alignment.CenterVertically)
            .padding(8.dp)
            .fillMaxWidth(0.7f)) {
            Text(
                item.name,
                style = MaterialTheme.typography.body1,
            )
            Text(
                stringResource(R.string.entry_kcal, item.calorieFor100g),
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
    
    data class CalorieDigits(val units: Int, val tens: Int, val hundreds: Int)
    
    val calDigits = run {
        if (foodEntry != null) {
            val digits = foodEntry.calorieFor100g.toString().map { it.digitToInt() }.toMutableList()
            while (digits.size < 3) {
                digits.add(0, 0)
            }
            CalorieDigits(
                digits.getOrNull(2) ?: 0,
                digits.getOrNull(1) ?: 0,
                digits.getOrNull(0) ?: 0,
            )
        } else {
            null
        }
    }
    
    var calUnit by remember { mutableStateOf(calDigits?.units ?: 0) }
    var calTens by remember { mutableStateOf(calDigits?.tens ?: 0) }
    var calHundreds by remember { mutableStateOf(calDigits?.hundreds ?: 0) }
    
    var canConfirm by remember { mutableStateOf(foodEntry?.name?.isNotEmpty() ?: false) }
    
    fun getCalorie() = calUnit + calTens * 10 + calHundreds * 100
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            elevation = 8.dp, shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                TextInput(
                    value = name,
                    onValueChange = {
                        name = it
                        canConfirm = it.text.isNotBlank()
                    },
                    label = { Text(stringResource(R.string.food_db_add_dialog_name_label)) },
                    placeholder = { Text(stringResource(R.string.food_db_add_dialog_name_placeholder)) },
                    modifier = Modifier.padding(8.dp),
                    isError = !canConfirm,
                    onSubmit = {
                        if (canConfirm) {
                            if (foodEntry == null) {
                                onValidate(FoodEntry(-1, name.text, getCalorie()))
                            } else {
                                foodEntry.name = name.text
                                foodEntry.calorieFor100g = getCalorie()
                                onEdit(foodEntry)
                            }
                        }
                    },
                    initialSelection = TextInputSelection.Full,
                    keyboardIcon = ImeAction.Next,
                    requestFocus = true,
                )
                Spacer(Modifier.height(16.dp))
                Row(modifier = Modifier.align(Alignment.CenterHorizontally), verticalAlignment = Alignment.CenterVertically) {
                    NumberPicker(
                        value = calHundreds, onValueChange = { calHundreds = it }, range = 0..9
                    )
                    NumberPicker(
                        value = calTens, onValueChange = { calTens = it }, range = 0..9
                    )
                    NumberPicker(
                        value = calUnit, onValueChange = { calUnit = it }, range = 0..9
                    )
                    Text(stringResource(R.string.kcal_for_100g), modifier = Modifier.padding(PaddingValues(horizontal = 8.dp)))
                }
                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismiss) { Text(stringResource(R.string.cancel), color = contentColorFor(MaterialTheme.colors.background)) }
                    if (foodEntry == null) {
                        TextButton(
                            { onValidate(FoodEntry(-1, name.text, getCalorie())) },
                            enabled = canConfirm
                        ) { Text(stringResource(R.string.create)) }
                    } else {
                        TextButton(
                            onClick = {
                                foodEntry.name = name.text
                                foodEntry.calorieFor100g = getCalorie()
                                onEdit(foodEntry)
                            }, enabled = canConfirm
                        ) { Text(stringResource(R.string.edit)) }
                    }
                }
            }
        }
    }
}