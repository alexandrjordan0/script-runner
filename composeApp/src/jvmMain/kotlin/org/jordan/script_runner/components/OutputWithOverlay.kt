package org.jordan.script_runner.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.style.AppColors

@Composable
fun OutputWithOverlay(
    outputValue: String,
    isRunning: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        IOField(
            value = outputValue,
            readOnly = true,
            output = true,
            modifier = Modifier.fillMaxSize(),
            onValueChange = { }
        )

        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isRunning) {
                CircularProgressIndicator(
                    modifier = Modifier.size(15.dp),
                    color = AppColors.TEXT_PRIMARY,
                    strokeWidth = 2.dp
                )
            }

            FilledIconButton(
                onClick = onToggle,
                modifier = Modifier.size(30.dp),
                shape = RoundedCornerShape(7.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isRunning) AppColors.RED else AppColors.IO_BACKGROUND,
                    contentColor = if (isRunning) AppColors.WHITE else AppColors.GREEN,
                )
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Stop" else "Run",
                    modifier = Modifier.size(15.dp)
                )
            }
        }
    }
}
