package dev.mpardo.dailycaloriecounter.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import com.chargemap.compose.numberpicker.NumberPicker

@Composable
fun NumberDigitInput(
    value: Number,
    onValueChange: (Int) -> Unit,
    nbDigit: Int,
) {
    var valueStr = value.toString()
    
    check(valueStr.length <= nbDigit) { "value($value) is too big for $nbDigit digits" }
    
    while (valueStr.length != nbDigit) {
        valueStr = "0$valueStr"
    }
    
    val digits = remember {
        Array(nbDigit) {
            mutableStateOf(if (it in valueStr.indices) valueStr[it].digitToInt() else 0)
        }
    }
    
    fun getValue() = digits
        .map { it.value.digitToChar() }
        .toCharArray()
        .concatToString()
        .toInt()
    
    
    Row {
        repeat(nbDigit) { digitIndex ->
            var digit by digits[digitIndex]
            NumberPicker(value = digit, onValueChange = {
                digit = it
                onValueChange(getValue())
            }, range = 0..9)
        }
    }
}