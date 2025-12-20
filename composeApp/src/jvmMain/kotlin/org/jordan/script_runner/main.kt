package org.jordan.script_runner

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ScriptRunner",
    ) {
        App()
    }
}