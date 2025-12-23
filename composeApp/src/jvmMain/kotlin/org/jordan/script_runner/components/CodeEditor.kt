package org.jordan.script_runner.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.TextFieldValue

/**
 * A composable function that provides a code editor interface with editable text content.
 *
 * @param value The current text to display in the editor.
 * @param onValueChange A callback invoked when the text content is modified.
 *                      It provides the new text value as a parameter.
 * @param modifier A [Modifier] to adjust the appearance or layout of the editor.
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
