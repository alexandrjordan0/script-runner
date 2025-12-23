package org.jordan.script_runner.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue

/**
 * A composable function that displays a code editor with editable text and optional focus handling.
 *
 * @param value The current [TextFieldValue] representing the text and selection state of the code editor.
 * @param onValueChange A callback invoked when the text or selection state of the code editor changes.
 * @param modifier A [Modifier] to be applied to the code editor container.
 * @param focusRequester An optional [FocusRequester] for programmatically requesting focus for the code editor.
 */
@Composable
fun CodeEditor(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester? = null
) {
    IOField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.then(
            if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier
        ),
        readOnly = false,
    )
}
