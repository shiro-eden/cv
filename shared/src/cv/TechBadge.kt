package org.example.cv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/** Сколько цветовых «слотов» чередуется между значками. */
private const val COLOR_SLOTS = 4

/** Размер иконки внутри значка. */
private val TECH_ICON_SIZE = 16.dp

/**
 * Значок технологии: монохромная иконка (в expanded — ещё и название) на
 * цветной «пилюле». Иконка окрашивается в цвет темы, поэтому подстраивается
 * под светлую и тёмную схемы. Цвет назначается самой технологии, а не позиции
 * в списке, поэтому Kotlin везде окрашен одинаково. Слот выбирается по хешу
 * имени между primary-, secondary- и tertiary-контейнерами и surfaceVariant —
 * все остаются в теме.
 */
@Composable
fun TechBadge(
    tech: Tech,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    val scheme = MaterialTheme.colorScheme
    val (container, onContainer) = when (tech.colorSlot()) {
        0 -> scheme.primaryContainer to scheme.onPrimaryContainer
        1 -> scheme.secondaryContainer to scheme.onSecondaryContainer
        2 -> scheme.tertiaryContainer to scheme.onTertiaryContainer
        else -> scheme.surfaceVariant to scheme.onSurfaceVariant
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = container,
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                horizontal = if (expanded) 10.dp else 6.dp,
                vertical = 6.dp,
            ),
        ) {
            Icon(
                imageVector = tech.icon,
                contentDescription = if (expanded) null else tech.label,
                tint = onContainer,
                modifier = Modifier.size(TECH_ICON_SIZE),
            )
            if (expanded) {
                Spacer(Modifier.width(6.dp))
                Text(
                    text = tech.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = onContainer,
                )
            }
        }
    }
}

/** Стабильный цветовой слот технологии: считается по её имени. */
private fun Tech.colorSlot(): Int = name.hashCode().mod(COLOR_SLOTS)

/** Ряд значков в плитке: не больше [maxVisible], остальное — «+N». */
@Composable
fun TechBadgeRow(
    techs: List<Tech>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 4,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        modifier = modifier,
    ) {
        techs.take(maxVisible).forEach { tech ->
            TechBadge(tech = tech)
        }
        if (techs.size > maxVisible) {
            Text(
                text = "+${techs.size - maxVisible}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Ряд полных названий технологий — для карточки проекта. */
@Composable
fun TechChipRow(
    techs: List<Tech>,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        techs.forEach { tech ->
            TechBadge(tech = tech, expanded = true)
        }
    }
}

@Preview(widthDp = 280, showBackground = true)
@Composable
private fun TechBadgeRowPreview() {
    CvTheme {
        TechBadgeRow(
            techs = cvProfile.projects.first().techs,
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Preview(widthDp = 480, showBackground = true)
@Composable
private fun TechChipRowPreview() {
    CvTheme {
        TechChipRow(
            techs = cvProfile.projects.first().techs,
            modifier = Modifier.padding(8.dp),
        )
    }
}
