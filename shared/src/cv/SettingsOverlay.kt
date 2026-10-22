package org.example.cv

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Оверлей настроек: тёмная тема и цветовая схема — "основные цвета" в духе
 * Android. Шестерёнка на месте, выбор палитры применяется сразу ко всему CV.
 */
@Composable
fun SettingsOverlay(
    visible: Boolean,
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    palette: CvPalette,
    onPaletteChange: (CvPalette) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
                    .pointerInput(Unit) {
                        detectTapGestures { onDismiss() }
                    },
            )
        }
        AnimatedVisibility(
            visible = visible,
            enter = scaleIn(
                initialScale = 0.85f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium,
                ),
            ) + fadeIn(tween(150)),
            exit = scaleOut(
                targetScale = 0.9f,
                animationSpec = tween(180),
            ) + fadeOut(tween(150)),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                SettingsCard(
                    darkTheme = darkTheme,
                    onDarkThemeChange = onDarkThemeChange,
                    palette = palette,
                    onPaletteChange = onPaletteChange,
                    onDismiss = onDismiss,
                )
            }
        }
    }
}

@Composable
private fun SettingsCard(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit,
    palette: CvPalette,
    onPaletteChange: (CvPalette) -> Unit,
    onDismiss: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(36.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = Modifier.widthIn(max = 440.dp),
    ) {
        Column(
            modifier = Modifier.padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = "Настройки",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = CvIcons.Close,
                        contentDescription = "Закрыть",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            SwitchRow(
                icon = CvIcons.Moon,
                title = "Тёмная тема",
                checked = darkTheme,
                onCheckedChange = onDarkThemeChange,
            )

            PaletteRow(palette = palette, onPaletteChange = onPaletteChange)

            Text(
                text = "Схема применяется сразу: светлый и тёмный варианты у каждой свои.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            FilledTonalButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text("Готово")
            }
        }
    }
}

/** Кружки выбора цветовой схемы — как "основные цвета" в Android. */
@Composable
private fun PaletteRow(
    palette: CvPalette,
    onPaletteChange: (CvPalette) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = CvIcons.Palette,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = "Основные цвета",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(start = 38.dp),
        ) {
            CvPalette.entries.forEach { candidate ->
                PaletteCircle(
                    palette = candidate,
                    selected = candidate == palette,
                    onClick = { onPaletteChange(candidate) },
                )
            }
        }
    }
}

/** Кружок-претендент: пастельный фон, у выбранного — обод и галочка. */
@Composable
private fun PaletteCircle(
    palette: CvPalette,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant
        },
        animationSpec = tween(200),
        label = "paletteBorderColor",
    )
    val borderWidth by animateDpAsState(
        targetValue = if (selected) 2.dp else 1.dp,
        animationSpec = tween(200),
        label = "paletteBorderWidth",
    )
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(palette.swatch)
            .border(borderWidth, borderColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Icon(
                imageVector = CvIcons.Check,
                contentDescription = "Выбрана схема \"${palette.label}\"",
                tint = palette.onSwatch,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun SettingsOverlayPreview() {
    CvTheme {
        SettingsOverlay(
            visible = true,
            darkTheme = false,
            onDarkThemeChange = {},
            palette = CvPalette.Purple,
            onPaletteChange = {},
            onDismiss = {},
        )
    }
}

/** Строка настроек с переключателем — как в Android. */
@Composable
private fun SwitchRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
