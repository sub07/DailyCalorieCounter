package dev.mpardo.dailycaloriecounter.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation

class NumberInputValue(internal val textValue: TextFieldValue) {
    val value: Number get() = textValue.text.toLong()
    val selection get() = textValue.selection
    val composition get() = textValue.composition
    
    constructor(number: Number, selection: TextRange = TextRange.Zero, composition: TextRange? = null) : this(
        TextFieldValue(
            number.toString(), selection, composition
        )
    )
    
    fun copy(
        value: Number = this.value,
        selection: TextRange = this.selection,
        composition: TextRange? = this.composition,
    ) = NumberInputValue(TextFieldValue(value.toString(), selection, composition))
}

@Composable
fun NumberInput(
    value: NumberInputValue,
    onValueChange: (NumberInputValue) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.OutlinedTextFieldShape,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    focused: Boolean = false,
    initialSelection: TextInputSelection = TextInputSelection.None,
    onSubmit: ((Number) -> Unit)? = null,
    keyboardIcon: ImeAction = ImeAction.Done,
    validation: (NumberInputValue) -> Boolean = { true },
) {
    
    TextInput(
        value = value.textValue,
        onValueChange = { it.text.toLongOrNull()?.let { _ -> onValueChange(NumberInputValue(it)) } },
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
        focused = focused,
        initialSelection = initialSelection,
        onSubmit = {
            onSubmit?.let { func ->
                it.toLongOrNull()?.let(func)
            }
        },
        keyboardIcon = keyboardIcon,
        validation = { validation(NumberInputValue(it)) }
    )
    
}