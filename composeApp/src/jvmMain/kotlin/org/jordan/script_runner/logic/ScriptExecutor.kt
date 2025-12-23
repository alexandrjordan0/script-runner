package org.jordan.script_runner.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

/**
 * Executes Kotlin scripts using the Kotlin compiler.
 */
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

            val env = processBuilder.environment()
            env["JAVA_TOOL_OPTIONS"] = "-Dfile.encoding=UTF-8"

            val process = processBuilder.start()

            coroutineScope {
                // Read the standard output stream
                launch(Dispatchers.IO) {
                    readStream(process.inputStream) { line ->
                        if (!line.contains("Picked up JAVA_TOOL_OPTIONS")) {
                            onOutput(line + "\n")
                        }
                    }
                }

                // Read the error output stream
                launch(Dispatchers.IO) {
                    readStream(process.errorStream) { line ->
                        if (!line.contains("Picked up JAVA_TOOL_OPTIONS")) {
                            onOutput("<ERR>${line}</ERR>\n")
                        }
                    }
                }

                process.waitFor()
            }

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

    private fun readStream(stream: InputStream, onLine: (String) -> Unit) {
        val reader = BufferedReader(InputStreamReader(stream, StandardCharsets.UTF_8))
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                line?.let { onLine(it) }
            }
        } catch (_: Exception) {
        }
    }
}
