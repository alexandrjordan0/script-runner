package org.jordan.script_runner

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jordan.script_runner.components.AppFrame
import org.jordan.script_runner.logic.AppPreferences

fun main() = application {
    val windowState = rememberWindowState()

    val initialColor = remember { AppPreferences.loadAccentColor() }
    var accentColor by remember { mutableStateOf(initialColor) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ScriptRunner",
        state = windowState,
        undecorated = true,
        transparent = true,
        icon = null
    ) {
        AppFrame(
            windowState = windowState,
            onCloseRequest = ::exitApplication,
            accentColor = accentColor,
            onAccentColorChange = { newColor ->
                accentColor = newColor
                AppPreferences.saveAccentColor(newColor)
            }
        ) {
            App()
        }
    }
}
