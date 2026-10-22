package org.example.cv

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.unit.dp

/** Аннотация ссылки в AnnotatedString. */
private const val TAG_URL = "url"

private val LINK = Regex("\\[([^\\]]+)\\]\\(([^)\\s]+)\\)")
private val BOLD = Regex("\\*\\*([^*]+)\\*\\*")
private val CODE = Regex("`([^`]+)`")

private data class InlineStyles(
    val link: SpanStyle,
    val code: SpanStyle,
    val bold: SpanStyle,
)

@Composable
private fun rememberInlineStyles(): InlineStyles {
    val scheme = MaterialTheme.colorScheme
    return remember(scheme) {
        InlineStyles(
            link = SpanStyle(
                color = scheme.primary,
                textDecoration = TextDecoration.Underline,
            ),
            code = SpanStyle(
                fontFamily = FontFamily.Monospace,
                background = scheme.surfaceContainerHighest,
                color = scheme.onSurface,
            ),
            bold = SpanStyle(fontWeight = FontWeight.Bold),
        )
    }
}

/**
 * Инлайн-разметка: **жирный**, `код`, [ссылка](https://…). Ссылки получают
 * аннотацию TAG_URL — обработчик тапов в [Paragraph] открывает их в браузере.
 */
private fun inlineMarkdown(text: String, styles: InlineStyles): AnnotatedString =
    buildAnnotatedString {
        var cursor = 0
        while (cursor < text.length) {
            val link = LINK.find(text, cursor)
            val bold = BOLD.find(text, cursor)
            val code = CODE.find(text, cursor)
            // 0 — ссылка, 1 — жирный, 2 — код; берём самое левое вхождение
            val earliest = buildList {
                link?.let { add(0 to it) }
                bold?.let { add(1 to it) }
                code?.let { add(2 to it) }
            }.minByOrNull { it.second.range.first }

            if (earliest == null) {
                append(text.substring(cursor))
                break
            }
            val (kind, match) = earliest
            append(text.substring(cursor, match.range.first))
            when (kind) {
                0 -> {
                    pushStringAnnotation(TAG_URL, match.groupValues[2])
                    withStyle(styles.link) { append(match.groupValues[1]) }
                    pop()
                }
                1 -> withStyle(styles.bold) { append(match.groupValues[1]) }
                else -> withStyle(styles.code) { append(match.groupValues[1]) }
            }
            cursor = match.range.last + 1
        }
    }

/**
 * Мини-рендер Markdown, ровно столько, сколько нужно README проектов:
 * заголовки #/##/###, абзацы, списки «- », блоки кода ``` и инлайн-разметка.
 */
@Composable
fun MarkdownText(markdown: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        val lines = markdown.trim().lines()
        var index = 0
        while (index < lines.size) {
            val line = lines[index]
            when {
                line.trimStart().startsWith("```") -> {
                    val code = StringBuilder()
                    index++
                    while (index < lines.size && !lines[index].trimStart().startsWith("```")) {
                        code.appendLine(lines[index])
                        index++
                    }
                    index++ // закрывающая ```
                    CodeBlock(code.toString().trimEnd())
                }
                line.startsWith("### ") -> {
                    Heading(line.removePrefix("### "), MaterialTheme.typography.titleMedium)
                }
                line.startsWith("## ") -> {
                    Heading(line.removePrefix("## "), MaterialTheme.typography.titleLarge)
                }
                line.startsWith("# ") -> {
                    Heading(line.removePrefix("# "), MaterialTheme.typography.headlineSmall)
                }
                line.startsWith("- ") || line.startsWith("* ") -> {
                    ListItem(line.substring(2))
                }
                line.isBlank() -> Unit
                else -> Paragraph(line)
            }
            index++
        }
    }
}

@Composable
private fun Heading(text: String, style: TextStyle) {
    Text(
        text = inlineMarkdown(text, rememberInlineStyles()),
        style = style,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

/** Абзац с кликабельными ссылками. */
@Composable
private fun Paragraph(text: String) {
    val uriHandler = LocalUriHandler.current
    val styles = rememberInlineStyles()
    val annotated = remember(text, styles) { inlineMarkdown(text, styles) }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    Text(
        text = annotated,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        onTextLayout = { layoutResult.value = it },
        modifier = Modifier.pointerInput(annotated) {
            detectTapGestures { position ->
                layoutResult.value?.let { layout ->
                    val offset = layout.getOffsetForPosition(position)
                    annotated
                        .getStringAnnotations(TAG_URL, offset, offset)
                        .firstOrNull()
                        ?.let { annotation ->
                            runCatching { uriHandler.openUri(annotation.item) }
                        }
                }
            }
        },
    )
}

@Composable
private fun ListItem(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "•",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = inlineMarkdown(text, rememberInlineStyles()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

/** Блок кода: моноширинный, с горизонтальной прокруткой. */
@Composable
private fun CodeBlock(code: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
    ) {
        Text(
            text = code,
            style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(12.dp),
        )
    }
}
