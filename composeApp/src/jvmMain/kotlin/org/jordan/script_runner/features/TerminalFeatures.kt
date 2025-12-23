package org.jordan.script_runner.features


import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import org.jordan.script_runner.style.AppColors

/**
 * A visual transformation implementation for highlighting terminal output text
 * with specific styling rules. This class processes the input text and applies
 * styles to elements like error messages and script file locations to enhance
 * readability and usability within a terminal-like UI.
 *
 * - Error messages, denoted by `<ERR>` tags, are styled with a bold red color
 *   and the tags themselves are hidden from the display.
 * - File location references in the format `filename.kts:line:column` are styled
 *   with an underline and a clickable hyperlink annotation.
 *
 * This class can be integrated with UI components for displaying stylized terminal
 * outputs, making it easier to identify errors and navigate to the related file
 * and line locations.
 */
class TerminalHighlighter : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            highlightTerminal(text.text),
            OffsetMapping.Identity
        )
    }
}

/**
 * Highlights terminal output; styles errors and locations
 */
private fun highlightTerminal(code: String): AnnotatedString = buildAnnotatedString {
    append(code)

    val errorTagRegex = Regex("<ERR>(.*?)</ERR>")
    errorTagRegex.findAll(code).forEach { match ->
        // Change the color of the error to red
        addStyle(
            style = SpanStyle(
                color = AppColors.RED,
                fontWeight = FontWeight.SemiBold
            ),
            start = match.groups[1]!!.range.first,
            end = match.groups[1]!!.range.last + 1
        )

        // Hide the <ERR> tag
        addStyle(
            style = SpanStyle(
                color = Color.Transparent,
                fontSize = 0.sp,
                letterSpacing = 0.sp
            ),
            start = match.range.first,
            end = match.range.first + 5
        )

        // Hide the </ERR> tag
        addStyle(
            style = SpanStyle(
                color = Color.Transparent,
                fontSize = 0.sp,
                letterSpacing = 0.sp
            ),
            start = match.range.last - 5,
            end = match.range.last + 1
        )
    }

    val locationRegex = Regex("""((?:[a-zA-Z]:)?[^:\s]+\.kts):(\d+)(?::(\d+))?""")
    locationRegex.findAll(code).forEach { match ->
        val line = match.groups[2]?.value ?: "1"
        val col = match.groups[3]?.value ?: "1"

        // Style the script location blue
        addStyle(
            style = SpanStyle(
                color = AppColors.LINK,
                textDecoration = TextDecoration.Underline
            ),
            start = match.range.first,
            end = match.range.last + 1
        )

        // Add url tag for click handler for navigation
        addStringAnnotation(
            tag = "URL",
            annotation = "$line:$col",
            start = match.range.first,
            end = match.range.last + 1
        )
    }
}