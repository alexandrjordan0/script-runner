package org.jordan.script_runner.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.features.KotlinHighlighter
import org.jordan.script_runner.features.TerminalHighlighter
import org.jordan.script_runner.style.AppColors
import org.jordan.script_runner.style.textStyle

/**
 * A composable function that provides an input/output text field with support for syntax highlighting,
 * scrolling, line numbers, and an optional top bar for additional UI controls.
 *
 * @param value The current [TextFieldValue] of the text field, representing the text and selection state.
 * @param onValueChange A callback invoked when the value of the text field changes.
 * @param modifier A [Modifier] to be applied to the text field's container.
 * @param readOnly A flag indicating whether the text field is read-only.
 * @param isOutput A flag indicating if the text field is used for displaying output. This affects the visual transformation applied to the text.
 * @param isSoftWrap A flag indicating whether soft wrapping of text is enabled.
 * @param scrollState An optional [ScrollState] for configuring vertical scroll behavior. If null, an internal state will be used.
 * @param topBar An optional composable function to provide a top bar within the container for additional UI elements.
 * @param onNavigate An optional callback triggered when navigating to a specific part of the text, typically used for hyperlink navigation.
 */
@Composable
fun IOField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    isOutput: Boolean = false,
    isSoftWrap: Boolean = false,
    scrollState: ScrollState? = null,
    topBar: (@Composable () -> Unit)? = null,
    onNavigate: ((Int, Int) -> Unit)? = null,
) {
    val roundedCornerShape = RoundedCornerShape(8.dp)
    val horizontalScroll = rememberScrollState()

    val verticalScroll = scrollState ?: rememberScrollState()

    val lineCount by remember(value.text) {
        derivedStateOf { value.text.count { it == '\n' } + 1 }
    }

    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        textStyle = textStyle,
        cursorBrush = SolidColor(AppColors.TEXT_PRIMARY),
        visualTransformation = remember(isOutput) {
            if (isOutput) TerminalHighlighter() else KotlinHighlighter()
        },
        onTextLayout = { layoutResult.value = it },
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
                    // Top bar with a divider under it
                    if (topBar != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
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

                    // Input or output text field
                    Box(modifier = Modifier.fillMaxWidth().weight(1f).padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(verticalScroll)
                        ) {
                            if (!isOutput) {
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

                                Box(
                                    modifier = Modifier.terminalLinkHandler(
                                        isOutput = isOutput,
                                        text = value.text,
                                        layoutResult = layoutResult,
                                        onNavigate = onNavigate
                                    )
                                ) {
                                    innerTextField()
                                }
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
 * A composable function that displays line numbers up to the specified count.
 *
 * @param lineCount The total number of lines for which the line numbers will be displayed.
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

/**
 * Adds a terminal link handler to the `Modifier`, enabling detection of clickable links
 * within terminal-like text output and navigation to specified locations.
 *
 * This modifier processes text annotations to check for "URL" tags, extracts line and
 * column information (if present), and triggers the provided `onNavigate` callback
 * when a link is tapped.
 *
 * @param isOutput A flag indicating if the text is output. If false, the handler is disabled.
 * @param text The terminal text to be processed for link detection.
 * @param layoutResult A mutable state holding the `TextLayoutResult`, which provides
 *                     positioning and layout details for the text.
 * @param onNavigate A callback function invoked when a link is clicked. It provides
 *                   the extracted line and column numbers as parameters. Can be null.
 * @return An updated `Modifier` instance with the terminal link handling functionality.
 */
private fun Modifier.terminalLinkHandler(
    isOutput: Boolean,
    text: String,
    layoutResult: MutableState<TextLayoutResult?>,
    onNavigate: ((Int, Int) -> Unit)?
): Modifier = this.pointerInput(isOutput, text) {
    if (isOutput) {
        detectTapGestures { pos ->
            layoutResult.value?.let { layout ->
                val offset = layout.getOffsetForPosition(pos)

                val highlighter = TerminalHighlighter()
                val transformed = highlighter.filter(AnnotatedString(text))
                val annotatedText = transformed.text

                if (offset >= 0 && offset < annotatedText.length) {
                    annotatedText.getStringAnnotations("URL", offset, offset)
                        .firstOrNull()?.let { annotation ->
                            val parts = annotation.item.split(":")
                            if (parts.size == 2) {
                                val line = parts[0].toIntOrNull() ?: 1
                                val col = parts[1].toIntOrNull() ?: 1
                                onNavigate?.invoke(line, col)
                            }
                        }
                }
            }
        }
    }
}
