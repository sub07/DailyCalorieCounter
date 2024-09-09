package me.mpardo.dailycaloriecounter.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.mpardo.dailycaloriecounter.R
import me.mpardo.dailycaloriecounter.model.FoodRecord
import me.mpardo.dailycaloriecounter.ui.component.DividedLazyColumn

@Composable
fun FoodRecordListScreen(
    records: List<FoodRecord>,
    onRecordDelete: (FoodRecord) -> Unit,
) {
    if (records.isEmpty()) {
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.no_record),
                    style = MaterialTheme.typography.body1.copy(textAlign = TextAlign.Center)
                )
            }
        }
    } else {
        Card(modifier = Modifier.padding(8.dp)) {
            DividedLazyColumn(list = records, modifier = Modifier.padding(8.dp)) {
                FoodRecordItem(it, onDelete = { onRecordDelete(it) })
            }
        }
    }
}

@Composable
fun FoodRecordItem(
    item: FoodRecord,
    onDelete: () -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(8.dp)
                .fillMaxWidth(0.7f)
        ) {
            Text(
                stringResource(R.string.records_mass, item.mass, item.food.name),
                style = MaterialTheme.typography.body1,
            )
            Text(
                stringResource(
                    R.string.kcal_label,
                    (item.mass * (item.food.energy.value / 100f)).toInt()
                ),
                style = MaterialTheme.typography.caption,
            )
        }
        Row {
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete Food Record")
            }
        }
    }
}