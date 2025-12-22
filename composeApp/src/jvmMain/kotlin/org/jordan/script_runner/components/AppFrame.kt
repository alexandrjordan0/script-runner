package org.jordan.script_runner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import org.jordan.script_runner.style.AppColors

@Composable
fun WindowScope.AppFrame(
    windowState: WindowState,
    onCloseRequest: () -> Unit,
    titleBarColor: Color = AppColors.FRAME,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(titleBarColor)
    ) {
        WindowDraggableArea {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = { windowState.isMinimized = true },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Minimize,
                        contentDescription = "Min",
                        tint = Color.Gray,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                IconButton(
                    onClick = {
                        windowState.placement = if (windowState.placement == WindowPlacement.Maximized) {
                            WindowPlacement.Floating
                        } else {
                            WindowPlacement.Maximized
                        }
                    },
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = if (windowState.placement == WindowPlacement.Maximized)
                            Icons.Default.FilterNone else Icons.Default.CropSquare,
                        contentDescription = "Max",
                        tint = AppColors.LIGHT_GRAY,
                        modifier = Modifier.size(14.dp)
                    )
                }

                IconButton(
                    onClick = onCloseRequest,
                    modifier = Modifier.size(30.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = AppColors.LIGHT_GRAY,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}