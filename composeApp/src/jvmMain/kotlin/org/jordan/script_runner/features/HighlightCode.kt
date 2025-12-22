package org.jordan.script_runner.features

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import org.jordan.script_runner.style.AppColors

private val keywords = setOf(
    "fun", "var", "val", "if", "else", "for", "while", "return", "class", "object", "package", "import"
)

/**
 * A visual transformation implementation for applying Kotlin syntax highlighting
 * to a given text. It uses the `highlightKotlin` function to style specific
 * syntax elements such as keywords, strings, comments, numbers, and variable names
 * within Kotlin code.
 *
 * This class is intended to be used in UI components requiring syntax highlighting
 * for Kotlin source code, enabling an enhanced and aesthetically pleasing code
 * presentation.
 *
 * @constructor Creates an instance of `KotlinHighlighter`.
 */
class KotlinHighlighter : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            highlightKotlin(text.text),
            OffsetMapping.Identity
        )
    }
}

/**
 * Highlights Kotlin syntax elements such as keywords, strings, comments, numbers,
 * and variable names in the provided code string. The method applies corresponding
 * styles to each syntax element within an annotated string.
 *
 * @param code The input string containing Kotlin code to be highlighted.
 * @return An [AnnotatedString] with styles applied for syntax highlighting.
 */
private fun highlightKotlin(code: String): AnnotatedString = buildAnnotatedString {
    append(code)

    // Highlight keywords
    val keywordRegex = Regex("\\b(${keywords.joinToString("|")})\\b")
    keywordRegex.findAll(code).forEach { match ->
        addStyle(
            style = SpanStyle(color = AppColors.KEYWORD, fontWeight = FontWeight.Bold),
            start = match.range.first,
            end = match.range.last + 1
        )
    }

    // Highlight strings
    Regex("\".*?\"").findAll(code).forEach { match ->
        addStyle(
            style = SpanStyle(color = AppColors.STRING),
            start = match.range.first,
            end = match.range.last + 1
        )
    }

    // Highlight comments
    Regex("//.*").findAll(code).forEach { match ->
        addStyle(
            style = SpanStyle(color = AppColors.COMMENT),
            start = match.range.first,
            end = match.range.last + 1
        )
    }

    // Highlight numbers
    Regex("\\b[0-9]+\\b").findAll(code).forEach { match ->
        addStyle(
            style = SpanStyle(color = AppColors.NUMBER),
            start = match.range.first,
            end = match.range.last + 1
        )
    }

    // Highlight variable_names
    val variableRegex = Regex("\\b(?:val|var)\\s+([A-Za-z_][A-Za-z0-9_]*)")
    variableRegex.findAll(code).forEach { match ->
        val varName = match.groups[1]
        if (varName != null) {
            val start = varName.range.first
            val end = varName.range.last + 1
            addStyle(
                style = SpanStyle(color = AppColors.VARIABLE_NAME),
                start = start,
                end = end
            )
        }
    }
}