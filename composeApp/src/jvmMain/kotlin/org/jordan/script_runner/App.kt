package org.jordan.script_runner

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.skiko.Cursor
import org.jordan.script_runner.components.IOField
import org.jordan.script_runner.components.TerminalOutput
import org.jordan.script_runner.style.AppColors

@Composable
@Preview
fun App() {
    var scriptValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var splitRatio by remember { mutableStateOf(0.75f) }
    var isSoftWrap by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()
    var scriptJob by remember { mutableStateOf<Job?>(null) }

    val inputFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        inputFocusRequester.requestFocus()
    }

    val terminalScrollState = rememberScrollState()

    LaunchedEffect(outputValue) {
        if (outputValue.isNotEmpty()) {
            terminalScrollState.animateScrollTo(terminalScrollState.maxValue)
        }
    }

    fun toggleExecution() {
        if (isRunning) {
            scriptJob?.cancel()
            isRunning = false
            outputValue += "\n[Terminated by user]"
        } else {
            isRunning = true
            outputValue = ""

            scriptJob = scope.launch {
                val code = ScriptExecutor.runScript(
                    scriptContent = scriptValue,
                    onOutput = { outputValue += it },
                    onError = { outputValue += it }
                )

                isRunning = false

                if (code != -1) {
                    outputValue += "\n[Failed with exit code $code]"
                }
            }
        }
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = AppColors.BACKGROUND) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalHeight = constraints.maxHeight.toFloat()

                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                    ) {
                        IOField(
                            value = scriptValue,
                            onValueChange = { if (!isRunning) scriptValue = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(splitRatio)
                                .focusRequester(inputFocusRequester)
                                .padding(bottom = 8.dp),
                            readOnly = isRunning,
                        )

                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .fillMaxHeight()
                                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR)))
                                .draggable(
                                    orientation = Orientation.Vertical,
                                    state = rememberDraggableState { delta ->
                                        val newRatio = splitRatio + (delta / totalHeight)
                                        splitRatio = newRatio.coerceIn(0.1f, 0.9f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            HorizontalDivider(
                                color = AppColors.BORDER,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth().padding(bottom = 4.dp)
                            )
                        }

                        TerminalOutput(
                            outputValue = outputValue,
                            isRunning = isRunning,
                            onToggle = {
                                toggleExecution()
                            },
                            onScrollToBottom = {
                                scope.launch {
                                    terminalScrollState.animateScrollTo(terminalScrollState.maxValue)
                                }
                            },
                            scrollState = terminalScrollState,
                            isSoftWrap = isSoftWrap,
                            onToggleSoftWrap = { isSoftWrap = !isSoftWrap },
                            onClear = { outputValue = "" },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f - splitRatio)
                        )
                    }
                }
            }
        }
    }
}