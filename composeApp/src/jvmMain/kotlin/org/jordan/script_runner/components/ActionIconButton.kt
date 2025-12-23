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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionIconButton(
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
