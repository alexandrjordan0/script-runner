package org.jordan.script_runner.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue

/**
 * A composable function that displays terminal-like output within a styled interface,
 * allowing for soft wrapping, scrolling, and other configurable options. It includes a
 * top bar for additional actions such as toggling soft wrap, clearing output, and more.
 *
 * @param outputValue The text content that represents the terminal output being displayed.
 * @param isRunning A Boolean indicating whether the terminal process is currently running.
 *                  This is typically used to control the state of the UI.
 * @param onToggle Callback invoked when the user interacts with the toggle button
 *                 (e.g., to start or stop the terminal process).
 * @param onClear Callback invoked when the clear action is triggered, typically used
 *                to clear the terminal output.
 * @param onScrollToBottom Callback invoked when the user triggers a scroll-to-bottom action,
 *                         useful in keeping the view scrolled to the latest output.
 * @param isSoftWrap A Boolean determining whether text content should wrap within the
 *                   available width or allow horizontal scrolling for long lines.
 * @param scrollState Optional vertical scroll state to externally manage the scroll position.
 *                    If null, an internal scroll state is used by default.
 * @param onToggleSoftWrap Callback invoked when the user toggles the soft wrap setting.
 * @param modifier [Modifier] to be applied to the root composable, allowing for layout
 *                 customization and styling.
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