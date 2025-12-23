package org.jordan.script_runner.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    IOField(
        value = value,
        onValueChange = onValueChange,
        readOnly = false,
        output = false,
        modifier = modifier,
        topBar = {
            // empty top bar for the slick line design
        }
    )
}
