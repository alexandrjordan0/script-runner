package org.jordan.script_runner

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

object ScriptExecutor {
    suspend fun runScript(
        scriptContent: String,
        onOutput: (String) -> Unit,
        onError: (String) -> Unit
    ): Int = withContext(Dispatchers.IO) {
        val tempFile = File.createTempFile("script", ".kts")

        try {
            tempFile.writeText(scriptContent)

            val isWindows = System.getProperty("os.name").lowercase().contains("win")

            val command = if (isWindows) {
                listOf("cmd.exe", "/c", "kotlinc", "-script", tempFile.absolutePath)
            } else {
                listOf("kotlinc", "-script", tempFile.absolutePath)
            }

            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true)

            val env = processBuilder.environment()
            env["JAVA_TOOL_OPTIONS"] = "-Dfile.encoding=UTF-8"

            val process = processBuilder.start()

            val reader = BufferedReader(InputStreamReader(process.inputStream, StandardCharsets.UTF_8))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                if (line != null && !line.contains("Picked up JAVA_TOOL_OPTIONS")) {
                    if (line.contains(": error:") || line.contains("Exception")) {
                        onOutput("<ERR>${line}</ERR>\n")
                    } else {
                        onOutput(line + "\n")
                    }
                }
            }

            process.waitFor()
            return@withContext process.exitValue()

        } catch (e: Exception) {
            onError("Execution failed: ${e.message}\n")
            return@withContext -1
        } finally {
            if (tempFile.exists()) {
                tempFile.delete()
            }
        }
    }
}
