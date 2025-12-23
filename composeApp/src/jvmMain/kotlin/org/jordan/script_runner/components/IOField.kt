package org.jordan.script_runner.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.features.KotlinHighlighter
import org.jordan.script_runner.features.TerminalHighlighter
import org.jordan.script_runner.style.AppColors
import org.jordan.script_runner.style.textStyle

/**
A composable function for creating an input/output text field with optional features
 * such as scrollbars, syntax highlighting, and line numbers. This component is versatile
 * and supports both read-only and editable content, along with customization for soft wrapping
 * or single-line presentation.
 *
 * @param value The current text content displayed in the field.
 * @param modifier Modifier to be applied to the text field.
 * @param readOnly If true, the text field is non-editable and acts as a read-only output.
 * @param output If true, the text field displays content using terminal-style highlighting.
 *               Otherwise, Kotlin code syntax highlighting is applied.
 * @param isSoftWrap If true, the text wraps within the available width. If false, horizontal
 *                   scrolling is enabled when the text exceeds the visible area.
 * @param scrollState Optional vertical scroll state to manage the vertical scroll position
 *                    externally. If omitted, an internal scroll state is used.
 * @param onValueChange A callback invoked when the text content changes. It receives the new
 *                      text value as a parameter.
 */
@Composable
fun IOField(
    value: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    output: Boolean = false,
    isSoftWrap: Boolean = false,
    scrollState: ScrollState? = null,
    topBar: (@Composable () -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(16.dp)
    val horizontalScroll = rememberScrollState()

    val verticalScroll = scrollState ?: rememberScrollState()

    val lineCount by remember(value) {
        derivedStateOf { value.count { it == '\n' } + 1 }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(AppColors.TEXT_PRIMARY),
        visualTransformation = remember(output) {
            if (output) TerminalHighlighter() else KotlinHighlighter()
        },
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        decorationBox = { innerTextField ->


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.IO_BACKGROUND, shape = roundedCornerShape)
            ) {
                Column {
                    if (topBar != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                .background(AppColors.BACKGROUND)
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        ) {
                            topBar()
                        }

                        HorizontalDivider(
                            color = AppColors.BORDER,
                            thickness = 0.dp,
                            modifier = Modifier
                                .fillMaxWidth().padding(bottom = 3.dp)
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(verticalScroll)
                        ) {
                            if (!output) {
                                CodeLine(lineCount)
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .then(
                                        if (isSoftWrap) Modifier
                                        else Modifier.horizontalScroll(horizontalScroll)
                                    )
                            ) {
                                innerTextField()
                            }
                        }
                    }
                }

                if (!isSoftWrap) {
                    HorizontalScrollbar(
                        adapter = rememberScrollbarAdapter(horizontalScroll),
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                    )
                }

                VerticalScrollbar(
                    adapter = rememberScrollbarAdapter(verticalScroll),
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                )

            }
        }
    )
}

/**
 * A composable function that generates a visual representation of numbered code lines.
 *
 * @param lineCount The number of lines to display. This determines the range of line numbers shown.
 */
@Composable
fun CodeLine(lineCount: Int) {
    val lineNumbers = remember(lineCount) {
        (1..lineCount).joinToString("\n")
    }

    Text(
        text = lineNumbers,
        style = textStyle.copy(
            color = AppColors.TEXT_PRIMARY.copy(alpha = 0.5f),
            textAlign = TextAlign.End
        ),
        modifier = Modifier.padding(end = 12.dp)
    )
}
