package org.jordan.script_runner

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.skiko.Cursor
import org.jordan.script_runner.components.IOField
import org.jordan.script_runner.components.OutputWithOverlay
import org.jordan.script_runner.style.AppColors

@Composable
@Preview
fun App() {
    var scriptValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var isRunning by remember { mutableStateOf(false) }
    var splitRatio by remember { mutableStateOf(0.5f) }

    val inputFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        delay(100)
        inputFocusRequester.requestFocus()
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = AppColors.BACKGROUND) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val totalWidth = constraints.maxWidth.toFloat()
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IOField(
                            value = scriptValue,
                            modifier = Modifier.weight(splitRatio).focusRequester(inputFocusRequester),
                            readOnly = isRunning,
                        ) { scriptValue = it }

                        Box(
                            modifier = Modifier
                                .width(9.dp)
                                .fillMaxHeight()
                                .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR)))
                                .draggable(
                                    orientation = Orientation.Horizontal,
                                    state = rememberDraggableState { delta ->
                                        val newRatio = splitRatio + (delta / totalWidth)
                                        splitRatio = newRatio.coerceIn(0.1f, 0.9f)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            VerticalDivider(
                                color = AppColors.BORDER,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(vertical = 16.dp)
                            )
                        }

                        OutputWithOverlay(
                            outputValue = outputValue,
                            isRunning = isRunning,
                            onToggle = { isRunning = !isRunning },
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