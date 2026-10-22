package org.example.cv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Значок технологии: компактная «пилюля» с короткой подписью.
 * Цвет берётся по очереди из primary/secondary/tertiary контейнеров,
 * чтобы ряд значков не был монотонным и оставался в теме.
 */
@Composable
fun TechBadge(
    tech: Tech,
    index: Int,
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
) {
    val scheme = MaterialTheme.colorScheme
    val (container, onContainer) = when (index % 3) {
        0 -> scheme.primaryContainer to scheme.onPrimaryContainer
        1 -> scheme.secondaryContainer to scheme.onSecondaryContainer
        else -> scheme.tertiaryContainer to scheme.onTertiaryContainer
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = container,
        modifier = modifier,
    ) {
        Text(
            text = if (expanded) tech.label else tech.short,
            style = MaterialTheme.typography.labelSmall,
            color = onContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }
}

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
        techs.take(maxVisible).forEachIndexed { index, tech ->
            TechBadge(tech = tech, index = index)
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
        techs.forEachIndexed { index, tech ->
            TechBadge(tech = tech, index = index, expanded = true)
        }
    }
}
