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
import org.jordan.script_runner.style.AppColors

class TerminalHighlighter : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            highlightTerminal(text.text),
            OffsetMapping.Identity
        )
    }
}

private fun highlightTerminal(code: String): AnnotatedString = buildAnnotatedString {
    append(code)

    val errorTagRegex = Regex("<ERR>(.*?)</ERR>")
    errorTagRegex.findAll(code).forEach { match ->
        addStyle(
            style = SpanStyle(color = AppColors.RED, fontWeight = FontWeight.SemiBold),
            start = match.groups[1]!!.range.first,
            end = match.groups[1]!!.range.last + 1
        )
        addStyle(SpanStyle(color = Color.Transparent), match.range.first, match.range.first + 5)
        addStyle(SpanStyle(color = Color.Transparent), match.range.last - 5, match.range.last + 1)
    }

    val locationRegex = Regex("""script.*?\.kts:(\d+):(\d+)""")
    locationRegex.findAll(code).forEach { match ->
        val line = match.groups[1]?.value ?: "0"
        val col = match.groups[2]?.value ?: "0"

        addStyle(
            style = SpanStyle(
                color = AppColors.LINK,
                textDecoration = TextDecoration.Underline
            ),
            start = match.range.first,
            end = match.range.last + 1
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "$line:$col",
            start = match.range.first,
            end = match.range.last + 1
        )
    }
}