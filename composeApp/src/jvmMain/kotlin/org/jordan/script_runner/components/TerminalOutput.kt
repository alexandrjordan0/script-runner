package org.jordan.script_runner.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

/**
 * A composable function that displays terminal output with options for scrolling, soft wrapping,
 * and various actions like toggling, clearing the output, and navigating links.
 *
 * @param outputValue The value of the terminal output to be displayed.
 * @param isRunning A boolean indicating whether the terminal is currently running.
 * @param onToggle A callback invoked when toggling between running and stopped states.
 * @param onClear A callback triggered to clear the terminal output.
 * @param onScrollToBottom A callback invoked to scroll the terminal output to the bottom.
 * @param isSoftWrap A boolean indicating whether soft wrapping is enabled for the terminal content.
 * @param scrollState A custom scroll state to control vertical scrolling (optional).
 * @param onToggleSoftWrap A callback triggered to toggle the soft wrapping mode.
 * @param onNavigate A callback invoked to handle navigation events within the terminal output,
 * taking the x and y positions of the event as parameters.
 * @param modifier A [Modifier] applied to the terminal output container (optional).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalOutput(
    outputValue: String,
    isRunning: Boolean,
    onToggle: () -> Unit,
    onClear: () -> Unit,
    onScrollToBottom: () -> Unit,
    isSoftWrap: Boolean,
    scrollState: ScrollState? = null,
    onToggleSoftWrap: () -> Unit,
    onNavigate: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        IOField(
            value = TextFieldValue(outputValue),
            readOnly = true,
            isOutput = true,
            isSoftWrap = isSoftWrap,
            topBar = {
                TerminalTopBar(
                    isRunning = isRunning,
                    isSoftWrap = isSoftWrap,
                    onToggle = onToggle,
                    onToggleSoftWrap = onToggleSoftWrap,
                    onScrollToBottom = onScrollToBottom,
                    onClear = onClear
                )
            },
            modifier = Modifier.fillMaxSize(),
            scrollState = scrollState,
            onNavigate = onNavigate,
            onValueChange = { }
        )
    }
}