package org.example.cv

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Левая панель профиля: фото, кнопки связи, о себе, образование с прогрессом
 * обучения, навыки. Занимает примерно 1/4 ширины экрана (ширину задаёт вызов
 * через `Modifier.width(...)`).
 *
 * @param contentScrollable включает собственную прокрутку панели; выключите,
 * если панель уже лежит внутри внешнего скролла (узкая раскладка), иначе
 * внутренний скролл получит бесконечные ограничения и упадёт на Android.
 */
@Composable
fun ProfilePanel(
    profile: CvProfile,
    modifier: Modifier = Modifier,
    contentScrollable: Boolean = true,
) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (contentScrollable) {
                        Modifier.verticalScroll(rememberScrollState())
                    } else {
                        Modifier
                    }
                )
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            ProfileHeader(profile)
            ContactRow(profile.contacts)
            AboutSection(profile.about)
            EducationSection(profile.education)
            SkillsSection(profile.skills)
        }
    }
}

/** Фото в кольце + имя и роль по центру. */
@Composable
private fun ProfileHeader(profile: CvProfile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(144.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .border(4.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(6.dp),
        ) {
            ProfilePhoto(Modifier.fillMaxSize())
        }
        Spacer(Modifier.size(16.dp))
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = profile.role,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}

/**
 * Контакты как в приложении "Контакты": кнопка "Почта" и круглые tonal-кнопки
 * мессенджеров и соцсетей вместо длинного списка.
 */
@Composable
private fun ContactRow(contacts: List<Contact>) {
    val uriHandler = LocalUriHandler.current
    val open: (Contact) -> Unit = { contact -> runCatching { uriHandler.openUri(contact.url) } }
    val email = contacts.firstOrNull { it.type == ContactType.EMAIL }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (email != null) {
            FilledTonalButton(onClick = { open(email) }) {
                Icon(
                    imageVector = CvIcons.Email,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text("Почта")
            }
        }
        contacts
            .filter { it.type != ContactType.EMAIL }
            .forEach { contact ->
                FilledTonalIconButton(onClick = { open(contact) }) {
                    Icon(
                        imageVector = contact.type.icon(),
                        contentDescription = contact.type.label(),
                    )
                }
            }
    }
}

@Composable
private fun AboutSection(about: String) {
    Sectioned(Modifier.fillMaxWidth(), header = "О себе") {
        Text(
            text = about,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/** Образование: карточка в духе настроек Android — иконка в tonal-круге и прогресс курса. */
@Composable
private fun EducationSection(entries: List<EducationEntry>) {
    Sectioned(Modifier.fillMaxWidth(), header = "Образование") {
        entries.forEach { entry ->
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = CvIcons.GraduationCap,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = entry.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Text(
                                text = entry.detail,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    val course = entry.course
                    val totalCourses = entry.totalCourses
                    if (course != null && totalCourses != null && totalCourses > 0) {
                        SnakeProgress(
                            fraction = course.toFloat() / totalCourses,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "$course курс из $totalCourses",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Прогресс-полоса-"змейка" из Expressive-слайдеров: заполненная часть —
 * бегущая волна, дальше — затухшая прямая, в конце дорожки — точка-ограничитель
 * как в прогрессах Material 3. Стык волны и хвоста закрывает бегунок — узкая
 * вертикальная пилюля с зазорами с обеих сторон.
 */
@Composable
private fun SnakeProgress(fraction: Float, modifier: Modifier = Modifier) {
    // бег змейки: фаза линейно крутится по кругу
    val phase = rememberInfiniteTransition(label = "snake").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "phase",
    )
    val waveColor = MaterialTheme.colorScheme.primary
    val tailColor = waveColor.copy(alpha = 0.35f)
    val handleWidth = 5.dp
    val gap = 6.dp
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    val handleWidthPx = with(LocalDensity.current) { handleWidth.toPx() }
    val gapPx = with(LocalDensity.current) { gap.toPx() }
    val handleX = fraction * (trackWidthPx - handleWidthPx).coerceAtLeast(0f)

    Box(modifier.height(20.dp)) {
        Canvas(Modifier.fillMaxWidth().height(20.dp).onSizeChanged { trackWidthPx = it.width.toFloat() }) {
            val midY = size.height / 2
            val strokeWidth = 4.dp.toPx()
            val stopRadius = 3.dp.toPx()
            val stopGap = 4.dp.toPx()
            val trackEnd = (size.width - stopRadius * 2 - stopGap).coerceAtLeast(0f)
            // хвост: затухшая прямая от бегунка до точки-ограничителя
            val tailStart = handleX + handleWidthPx + gapPx
            if (tailStart < trackEnd) {
                drawLine(
                    color = tailColor,
                    start = Offset(tailStart, midY),
                    end = Offset(trackEnd, midY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round,
                )
            }
            drawCircle(
                color = waveColor,
                radius = stopRadius,
                center = Offset(trackEnd + stopGap + stopRadius, midY),
            )
            // змейка: квадратичные дуги, обрезанные по бегунок
            val waveEnd = (handleX - gapPx).coerceAtLeast(0f)
            if (waveEnd > 2f) {
                val amplitude = 6.dp.toPx()
                val step = 22.dp.toPx()
                val shift = phase.value * 2 * step // период бега = 2 шага волны
                val path = Path()
                var x = -2 * step // запас под сдвиг фазы
                path.moveTo(x, midY)
                var up = true
                while (x < waveEnd + 2 * step) {
                    val next = minOf(x + step, waveEnd + 2 * step)
                    path.quadraticTo(
                        x + (next - x) / 2,
                        midY + (if (up) -1f else 1f) * amplitude,
                        next,
                        midY,
                    )
                    up = !up
                    x = next
                }
                clipRect(0f, 0f, waveEnd, size.height) {
                    translate(left = -shift) { // волна "вытекает" влево
                        drawPath(
                            path,
                            color = waveColor,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .offset { IntOffset(handleX.roundToInt(), 2.dp.roundToPx()) }
                .size(handleWidth, 16.dp)
                .clip(RoundedCornerShape(50))
                .background(waveColor),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSection(skills: List<String>) {
    Sectioned(Modifier.fillMaxWidth(), header = "Навыки") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            skills.forEach { skill -> SkillChip(skill) }
        }
    }
}

/** Небольшая "пилюля" навыка в secondary-контейнере. */
@Composable
private fun SkillChip(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

fun ContactType.icon(): ImageVector = when (this) {
    ContactType.EMAIL -> CvIcons.Email
    ContactType.TELEGRAM -> CvIcons.Telegram
    ContactType.GITHUB -> CvIcons.GitHub
}

/** Человекочитаемое имя типа контакта — для описания кнопок и подписей. */
fun ContactType.label(): String = when (this) {
    ContactType.EMAIL -> "Почта"
    ContactType.TELEGRAM -> "Telegram"
    ContactType.GITHUB -> "GitHub"
}

/** Секция: заголовок + содержимое с небольшим отступом. */
@Composable
private fun Sectioned(
    modifier: Modifier = Modifier,
    header: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(header)
        content()
    }
}

@Preview(widthDp = 360, heightDp = 800, showBackground = true)
@Composable
private fun ProfilePanelPreview() {
    CvTheme {
        ProfilePanel(
            profile = cvProfile,
            modifier = Modifier.fillMaxSize().padding(16.dp),
        )
    }
}
