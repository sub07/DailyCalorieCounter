package me.mpardo.dailycaloriecounter.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import kotlinx.coroutines.delay

sealed interface TextInputSelection {
    object Full : TextInputSelection
    object None : TextInputSelection
    class Range(val start: Int, val end: Int) : TextInputSelection {
        init {
            if (start > end) {
                error("Start index is greater than end")
            }
        }
    }

    fun toTextRange(text: String): TextRange {
        return when (this) {
            is Full -> TextRange(0, text.length)
            is None -> TextRange.Zero
            is Range -> {
                if (this.start !in text.indices) error("Invalid start index")
                if (this.end !in 0..text.length) error("Invalid end index")
                TextRange(this.start, this.end)
            }
        }
    }
}

@Composable
fun TextInput(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
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
    initialSelection: TextInputSelection = TextInputSelection.None,
    focused: Boolean = false,
    onSubmit: ((String) -> Unit)? = null,
    keyboardIcon: ImeAction = ImeAction.Done,
    validation: (TextFieldValue) -> Boolean = { true },
) {
    check(keyboardIcon != ImeAction.None) { "Don't set keyboardIcon to None, on this single line text field and None correspond to Enter key" }
    val fr = remember { FocusRequester() }

    val keyboardActions = onSubmit?.let {
        KeyboardActions { onSubmit(value.text) }
    }

    var initialSelectionHandled by remember { mutableStateOf(false) }
    var initialSelectionReset by remember { mutableStateOf(false) }

    if (!initialSelectionHandled) {
        onValueChange(value.copy(selection = initialSelection.toTextRange(value.text)))
        initialSelectionHandled = true
    }


    val onValChange: (TextFieldValue) -> Unit = if (initialSelection != TextInputSelection.None) {
        {
            if (!initialSelectionReset) {
                onValueChange(it.copy(selection = TextRange(it.text.length)))
                initialSelectionReset = true
            } else {
                onValueChange(it)
            }
        }
    } else {
        { onValueChange(it) }
    }

    var isInputValid by remember {
        mutableStateOf(validation(value))
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValChange(it)
            isInputValid = validation(it)
        },
        modifier = modifier.focusRequester(fr),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError || !isInputValid,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(imeAction = keyboardIcon),
        keyboardActions = keyboardActions ?: KeyboardActions(),
        singleLine = true,
        interactionSource = interactionSource,
        shape = shape,
        colors = colors,
    )

    if (focused) {
        LaunchedEffect(Unit) {
            delay(150)
            fr.requestFocus()
        }
    }
}