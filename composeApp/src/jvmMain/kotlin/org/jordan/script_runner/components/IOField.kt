package org.jordan.script_runner.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.features.KotlinHighlighter
import org.jordan.script_runner.features.TerminalHighlighter
import org.jordan.script_runner.style.AppColors
import org.jordan.script_runner.style.textStyle

@Composable
fun IOField(
    value: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    output: Boolean = false,
    isSoftWrap: Boolean = false,
    scrollState: ScrollState? = null,
    onValueChange: (String) -> Unit
) {
    val roundedCornerShape = RoundedCornerShape(16.dp)
    val horizontalScroll = rememberScrollState()

    val verticalScroll = scrollState ?: rememberScrollState()

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
                    .border(
                        width = 1.dp,
                        color = AppColors.BORDER,
                        shape = roundedCornerShape
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(verticalScroll)
                ) {
                    if (!output) {
                        val lineCount = value.count { it == '\n' } + 1
                        Text(
                            text = (1..lineCount).joinToString("\n"),
                            style = textStyle.copy(
                                color = AppColors.TEXT_PRIMARY.copy(alpha = 0.5f),
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.padding(end = 12.dp)
                        )
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
