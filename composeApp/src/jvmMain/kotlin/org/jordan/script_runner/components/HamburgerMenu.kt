package org.jordan.script_runner.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.style.AppColors

/**
 * Composable function that displays a hamburger menu allowing the user to change the application's accent color.
 *
 * @param onAccentColorChange Callback triggered when a new accent color is selected.
 * The selected color is passed as a parameter to the callback.
 */
@Composable
fun HamburgerMenu(
    onAccentColorChange: (Color) -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    val availableColors = listOf(
        AppColors.ACCENT_PINK to "Pink",
        AppColors.ACCENT_PURPLE to "Purple",
        AppColors.ACCENT_BLUE to "Blue",
        AppColors.ACCENT_TEAL to "Teal",
        AppColors.ACCENT_GREEN to "Green",
        AppColors.ACCENT_GOLD to "Gold",
        AppColors.ACCENT_ORANGE to "Orange",
        AppColors.ACCENT_RED to "Red"
    )

    Box(modifier = Modifier.background(AppColors.TRANSPARENT)) {
        ActionIconButton(
            onClick = { menuExpanded = true },
            icon = Icons.Default.Menu,
            tooltip = "Change Theme",
            contentColor = AppColors.LIGHT_GRAY,
            backgroundColor = AppColors.TRANSPARENT,
        )

        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(16.dp)),
            colors = MaterialTheme.colors.copy(surface = AppColors.FRAME)
        ) {
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false },
                modifier = Modifier.border(1.dp, AppColors.IO_BACKGROUND, RoundedCornerShape(8.dp))
            ) {
                availableColors.forEach { (color, name) ->
                    DropdownMenuItem(
                        onClick = {
                            onAccentColorChange(color)
                            menuExpanded = false
                        },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(color, RoundedCornerShape(4.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = name,
                                style = MaterialTheme.typography.caption,
                                color = AppColors.TEXT_PRIMARY
                            )
                        }
                    }
                }
            }
        }
    }
}