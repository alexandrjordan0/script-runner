package org.jordan.script_runner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CropSquare
import androidx.compose.material.icons.filled.FilterNone
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import org.jordan.script_runner.style.AppColors

/**
 * A composable function that represents the main application window frame. It provides the
 * structure and layout for a custom window with draggable areas, an action bar, and user-defined content.
 *
 * @param windowState The current state of the window, including its size, placement, and minimized/maximized status.
 * @param onCloseRequest A callback function triggered when the user attempts to close the window.
 * @param accentColor The current color used to accent various visual elements of the frame.
 * @param onAccentColorChange A callback function triggered when the accent color is changed. The new color is passed as a parameter to the callback.
 * @param backgroundColor The color of the window frame background. By default, it uses the value defined in `AppColors.FRAME`.
 * @param content A composable lambda that provides the content to be displayed within the main body of the frame.
 */
@Composable
fun WindowScope.AppFrame(
    windowState: WindowState,
    onCloseRequest: () -> Unit,
    accentColor: Color,
    onAccentColorChange: (Color) -> Unit,
    backgroundColor: Color = AppColors.FRAME,
    content: @Composable () -> Unit
) {
    val backgroundBrush = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to accentColor.copy(alpha = 0.2f),
            0.1f to Color.Transparent,
            1.0f to Color.Transparent
        ),
        start = Offset(200f, 0f),
        end = Offset.Infinite
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = backgroundColor
        ) { }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundBrush)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 4.dp),
        ) {
            WindowDraggableArea {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Main Menu button
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        HamburgerMenu(
                            onAccentColorChange = onAccentColorChange
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Script Runner",
                            style = MaterialTheme.typography.labelLarge,
                            color = AppColors.TEXT_PRIMARY
                        )
                    }

                    Row {
                        // Minimize button
                        ActionIconButton(
                            onClick = { windowState.isMinimized = true },
                            icon = Icons.Default.Minimize,
                            tooltip = "Minimize",
                            contentColor = AppColors.LIGHT_GRAY,
                            backgroundColor = AppColors.TRANSPARENT
                        )

                        // Maximize / Restore button
                        ActionIconButton(
                            onClick = {
                                windowState.placement = if (windowState.placement == WindowPlacement.Maximized) {
                                    WindowPlacement.Floating
                                } else {
                                    WindowPlacement.Maximized
                                }
                            },
                            icon = if (windowState.placement == WindowPlacement.Maximized)
                                Icons.Default.FilterNone else Icons.Default.CropSquare,
                            tooltip = if (windowState.placement == WindowPlacement.Maximized) "Restore" else "Maximize",
                            contentColor = AppColors.LIGHT_GRAY,
                            backgroundColor = AppColors.TRANSPARENT
                        )

                        // Exit button
                        ActionIconButton(
                            onClick = onCloseRequest,
                            icon = Icons.Default.Close,
                            tooltip = "Close",
                            contentColor = AppColors.LIGHT_GRAY,
                            backgroundColor = AppColors.TRANSPARENT
                        )
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }
        }
    }
}