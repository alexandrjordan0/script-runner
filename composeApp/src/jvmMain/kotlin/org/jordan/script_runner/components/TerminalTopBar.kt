package org.jordan.script_runner.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VerticalAlignBottom
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.style.AppColors

/**
 * A composable function that displays a terminal top bar with controls for managing the terminal state.
 *
 * @param isRunning A boolean indicating whether the terminal is currently running.
 * @param isSoftWrap A boolean indicating whether soft wrap mode is enabled for terminal output.
 * @param onToggle A lambda function invoked when the start/stop button is clicked.
 * @param onToggleSoftWrap A lambda function invoked when the soft wrap toggle button is clicked.
 * @param onScrollToBottom A lambda function invoked when the scroll-to-bottom button is clicked.
 * @param onClear A lambda function invoked when the clear button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalTopBar(
    isRunning: Boolean,
    isSoftWrap: Boolean,
    onToggle: () -> Unit,
    onToggleSoftWrap: () -> Unit,
    onScrollToBottom: () -> Unit,
    onClear: () -> Unit
) {
    TopBar {
        // Start/stop button
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
                modifier = Modifier.size(30.dp),
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

        // Status text
        if (isRunning) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = AppColors.TEXT_PRIMARY,
                strokeWidth = 2.dp
            )
            Text(
                text = "Running...",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.TEXT_PRIMARY,
                fontWeight = FontWeight.Bold,
            )
        } else {
            Text(
                text = "Terminal Output",
                style = MaterialTheme.typography.labelSmall,
                color = AppColors.TEXT_PRIMARY,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Terminal control buttons
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
}