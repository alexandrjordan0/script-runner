package org.jordan.script_runner.components


import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.jordan.script_runner.style.AppColors

/**
 * A composable function that renders an action icon button with a tooltip.
 *
 * @param onClick A lambda function to be executed when the button is clicked.
 * @param icon The icon to display within the button.
 * @param tooltip Text to show in the tooltip when the user hovers over the button.
 * @param contentColor The color of the icon content within the button.
 * @param backgroundColor The background color of the button. Defaults to `AppColors.IO_BACKGROUND`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    tooltip: String,
    contentColor: Color,
    backgroundColor: Color = AppColors.IO_BACKGROUND,
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
            modifier = Modifier.size(30.dp),
            shape = RoundedCornerShape(8.dp),
            colors = IconButtonDefaults.filledIconButtonColors(
                containerColor = backgroundColor,
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
