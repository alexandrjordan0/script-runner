package org.jordan.script_runner.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.style.AppColors

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
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
                        TooltipAnchorPosition.Above
                    ),
                    tooltip = {
                        PlainTooltip(
                            containerColor = AppColors.FRAME,
                            contentColor = AppColors.WHITE
                        ) {
                            Text(if (isRunning) "Stop" else "Run")
                        }
                    },
                    state = rememberTooltipState()
                ) {
                    FilledIconButton(
                        onClick = onToggle,
                        modifier = Modifier.size(32.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = if (isRunning) AppColors.RED else AppColors.IO_BACKGROUND,
                            contentColor = if (isRunning) AppColors.WHITE else AppColors.GREEN,
                        )
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isRunning) "Stop" else "Run",
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }


                if (isRunning) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(14.dp),
                        color = AppColors.TEXT_PRIMARY,
                        strokeWidth = 2.dp
                    )
                    Text(
                        text = "Running...",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.TEXT_PRIMARY
                    )
                } else {
                    Text(
                        text = "Terminal Output",
                        style = MaterialTheme.typography.labelSmall,
                        color = AppColors.TEXT_PRIMARY
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ActionIconButton(
                    onClick = onToggleSoftWrap,
                    icon = Icons.AutoMirrored.Filled.Sort,
                    tooltip = "Toggle Soft Wrap",
                    contentColor = if (isSoftWrap) AppColors.GREEN else AppColors.TEXT_SECONDARY
                )

                ActionIconButton(
                    onClick = onScrollToBottom,
                    icon = Icons.Default.VerticalAlignBottom,
                    tooltip = "Scroll to Bottom",
                    contentColor = AppColors.TEXT_SECONDARY
                )

                ActionIconButton(
                    onClick = onClear,
                    icon = Icons.Default.Delete,
                    tooltip = "Clear Output",
                    contentColor = AppColors.TEXT_SECONDARY
                )
            }
            IOField(
                value = outputValue,
                readOnly = true,
                output = true,
                isSoftWrap = isSoftWrap,
                modifier = Modifier.fillMaxSize(),
                scrollState = scrollState,
                onValueChange = { }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tooltip: String,
    contentColor: Color
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(
            TooltipAnchorPosition.Above
        ),
        tooltip = {
            PlainTooltip(
                containerColor = AppColors.FRAME,
                contentColor = AppColors.WHITE
            ) {
                Text(tooltip)
            }
        },
        state = rememberTooltipState()
    ) {
        FilledIconButton(
            onClick = onClick,
            modifier = Modifier.size(32.dp),
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = AppColors.IO_BACKGROUND,
                contentColor = contentColor
            )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = tooltip,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
