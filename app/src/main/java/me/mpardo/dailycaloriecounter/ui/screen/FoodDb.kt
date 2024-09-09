package me.mpardo.dailycaloriecounter.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import me.mpardo.dailycaloriecounter.R
import me.mpardo.dailycaloriecounter.model.Carbohydrates
import me.mpardo.dailycaloriecounter.model.Energy
import me.mpardo.dailycaloriecounter.model.Fats
import me.mpardo.dailycaloriecounter.model.FoodEntry
import me.mpardo.dailycaloriecounter.model.FoodRecord
import me.mpardo.dailycaloriecounter.model.Proteins
import me.mpardo.dailycaloriecounter.model.Salt
import me.mpardo.dailycaloriecounter.ui.component.DividedLazyColumn
import me.mpardo.dailycaloriecounter.ui.component.NumberInput
import me.mpardo.dailycaloriecounter.ui.component.NumberInputValue
import me.mpardo.dailycaloriecounter.ui.component.TextInput
import me.mpardo.dailycaloriecounter.ui.component.TextInputSelection

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
                    text = stringResource(R.string.food_db_empty),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center)
                )
            }
        }
    } else {
        Card(modifier = Modifier.padding(8.dp)) {
            DividedLazyColumn(list = foods, modifier = Modifier.padding(8.dp)) {
                FoodDbItem(
                    it,
                    onEditClick = {
                        showAddFoodDialog = true
                        editedFood = it
                    },
                    onDeleteClick = {
                        if (foodRecords.map { r -> r.food }.contains(it)) {
                            val foodName = it.name.toLowerCase(Locale.current)
                            showErrorToast =
                                "Could not delete $foodName, delete all the records with it first"
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
        EditFoodDialog(editedFood, onDismiss = {
            showAddFoodDialog = false
            editedFood = null
        }, onValidate = {
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
fun FoodDbItem(item: FoodEntry, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
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
        }
        Row {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit Food Entry")
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Food Entry")
            }
        }
    }
}

@Composable
fun EditFoodDialog(
    foodEntry: FoodEntry? = null,
    onDismiss: () -> Unit = {},
    onValidate: (FoodEntry) -> Unit = {},
    onEdit: (FoodEntry) -> Unit = {}
) {
    var name by remember { mutableStateOf(TextFieldValue(foodEntry?.name ?: "")) }

    var energy by remember { mutableStateOf(NumberInputValue(foodEntry?.energy?.value ?: 0f)) }
    var energyFocused by remember { mutableStateOf(false) }

    var proteins by remember { mutableStateOf(NumberInputValue(foodEntry?.proteins?.value ?: 0f)) }
    var proteinsFocused by remember { mutableStateOf(false) }

    var fats by remember { mutableStateOf(NumberInputValue(foodEntry?.fats?.value ?: 0f)) }
    var fatsFocused by remember { mutableStateOf(false) }

    var carbohydrates by remember {
        mutableStateOf(
            NumberInputValue(
                foodEntry?.carbohydrates?.value ?: 0f
            )
        )
    }
    var carbohydratesFocused by remember { mutableStateOf(false) }

    var salt by remember { mutableStateOf(NumberInputValue(foodEntry?.salt?.value ?: 0f)) }
    var saltFocused by remember { mutableStateOf(false) }

    var canConfirm by remember { mutableStateOf(foodEntry?.name?.isNotEmpty() ?: false) }

    fun submit() {
        if (foodEntry == null) {
            onValidate(
                FoodEntry(
                    -1,
                    name.text,
                    Energy(energy.value.toFloat()),
                    Proteins(proteins.value.toFloat()),
                    Fats(fats.value.toFloat()),
                    Carbohydrates(carbohydrates.value.toFloat()),
                    Salt(salt.value.toFloat()),
                )
            )
        } else {
            val editedFood = foodEntry.copy(
                energy = Energy(energy.value.toFloat()),
                proteins = Proteins(proteins.value.toFloat()),
                fats = Fats(fats.value.toFloat()),
                carbohydrates = Carbohydrates(carbohydrates.value.toFloat()),
                salt = Salt(salt.value.toFloat()),
            )
            onEdit(editedFood)
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
                    validation = { it.text.isNotBlank() },
                    onSubmit = { energyFocused = true },
                    initialSelection = TextInputSelection.Full,
                    keyboardIcon = ImeAction.Next,
                    focused = true,
                )
                NumberInput(
                    value = energy,
                    onValueChange = {
                        energy = it
                        canConfirm = it.value.toFloat() > 0
                    },
                    label = { Text(text = stringResource(R.string.energy_name)) },
                    validation = { it.value.toInt() >= 0 },
                    focused = energyFocused,
                    keyboardIcon = ImeAction.Next,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { proteinsFocused = true },
                    allowFloat = true,
                )
                NumberInput(
                    value = proteins,
                    onValueChange = {
                        proteins = it
                    },
                    label = { Text(text = stringResource(R.string.protein_name)) },
                    focused = proteinsFocused,
                    keyboardIcon = ImeAction.Next,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { fatsFocused = true },
                    allowFloat = true,
                )
                NumberInput(
                    value = fats,
                    onValueChange = { fats = it },
                    label = { Text(text = stringResource(R.string.fats_name)) },
                    focused = fatsFocused,
                    keyboardIcon = ImeAction.Next,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { carbohydratesFocused = true },
                    allowFloat = true,
                )
                NumberInput(
                    value = carbohydrates,
                    onValueChange = { carbohydrates = it },
                    label = { Text(text = stringResource(R.string.carbohydrate_name)) },
                    focused = carbohydratesFocused,
                    keyboardIcon = ImeAction.Next,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { saltFocused = true },
                    allowFloat = true,
                )
                NumberInput(
                    value = salt,
                    onValueChange = { salt = it },
                    label = { Text(text = stringResource(R.string.salt_name)) },
                    focused = saltFocused,
                    keyboardIcon = ImeAction.Done,
                    initialSelection = TextInputSelection.Full,
                    onSubmit = { submit() },
                    allowFloat = true,
                )

                Row(modifier = Modifier.align(Alignment.End)) {
                    TextButton(onDismiss) {
                        Text(
                            stringResource(R.string.cancel),
                            color = contentColorFor(MaterialTheme.colors.background)
                        )
                    }
                    TextButton(
                        onClick = { submit() }, enabled = canConfirm
                    ) { Text(stringResource(if (foodEntry == null) R.string.create else R.string.edit)) }
                }
            }
        }
    }
}