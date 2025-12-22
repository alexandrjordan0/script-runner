package org.jordan.script_runner

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jordan.script_runner.components.AppFrame

fun main() = application {
    val windowState = rememberWindowState()

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
            onCloseRequest = ::exitApplication
        ) {
            App()
        }
    }
}
