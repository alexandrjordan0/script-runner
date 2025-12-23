package org.jordan.script_runner.logic

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import org.jordan.script_runner.style.AppColors
import java.io.File

/**
 * Class responsible for loading and saving application preferences.
 */
object AppPreferences {
    private val folder = File(System.getProperty("user.home"), ".script_runner")
    private val configFile = File(folder, "config.txt")

    fun loadAccentColor(): Color {
        return try {
            if (configFile.exists()) {
                val colorInt = configFile.readText().toInt()
                Color(colorInt)
            } else {
                AppColors.ACCENT_PINK
            }
        } catch (e: Exception) {
            e.printStackTrace()
            AppColors.ACCENT_PINK
        }
    }

    fun saveAccentColor(color: Color) {
        try {
            if (!folder.exists()) {
                folder.mkdirs()
            }
            configFile.writeText(color.toArgb().toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
