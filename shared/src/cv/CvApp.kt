package org.example.cv

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Порог "широкого" экрана, на котором хватает места на две панели. */
private val WIDE_BREAKPOINT = 720.dp

/**
 * Корень CV: слева профиль (≈1/4 ширины), справа "шторка" проектов.
 * Поверх всего — оверлеи карточки проекта и настроек.
 */
@Composable
fun CvApp(modifier: Modifier = Modifier) {
    var darkThemeOverride by remember { mutableStateOf<Boolean?>(null) }
    var palette by remember { mutableStateOf(CvPalette.Purple) }
    var selectedProject by remember { mutableStateOf<Project?>(null) }
    var settingsShown by remember { mutableStateOf(false) }
    val darkTheme = darkThemeOverride ?: isSystemInDarkTheme()

    CvTheme(darkTheme = darkTheme, palette = palette) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            BoxWithConstraints(Modifier.fillMaxSize().padding(24.dp)) {
                if (maxWidth >= WIDE_BREAKPOINT) {
                    WideLayout(
                        leftWidth = (maxWidth * 0.26f).coerceIn(300.dp, 420.dp),
                        onProjectClick = { selectedProject = it },
                        onOpenSettings = { settingsShown = true },
                    )
                } else {
                    NarrowLayout(
                        onProjectClick = { selectedProject = it },
                        onOpenSettings = { settingsShown = true },
                    )
                }

                ProjectDetailsOverlay(
                    project = selectedProject,
                    onDismiss = { selectedProject = null },
                )
                SettingsOverlay(
                    visible = settingsShown,
                    darkTheme = darkTheme,
                    onDarkThemeChange = { darkThemeOverride = it },
                    palette = palette,
                    onPaletteChange = { palette = it },
                    onDismiss = { settingsShown = false },
                )
            }
        }
    }
}

/** Две панели: левая занимает около 1/4 ширины, правая — всё остальное. */
@Composable
private fun WideLayout(
    leftWidth: Dp,
    onProjectClick: (Project) -> Unit,
    onOpenSettings: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ProfilePanel(
            profile = cvProfile,
            modifier = Modifier.width(leftWidth),
        )
        ProjectsPanel(
            projects = cvProfile.projects,
            onProjectClick = onProjectClick,
            onOpenSettings = onOpenSettings,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun NarrowLayout(
    onProjectClick: (Project) -> Unit,
    onOpenSettings: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        ProfilePanel(
            profile = cvProfile,
            contentScrollable = false,
            modifier = Modifier.fillMaxWidth(),
        )
        // На телефоне плитки не втискиваются по 4 — одна на страницу, зато
        // целая страница высоты: текст не приходится обрезать.
        ProjectsPanel(
            projects = cvProfile.projects,
            onProjectClick = onProjectClick,
            onOpenSettings = onOpenSettings,
            tilesPerPage = 1,
            modifier = Modifier.fillMaxWidth().height(560.dp),
        )
    }
}

/** Превью широкой раскладки: две панели рядом. */
@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun CvAppWidePreview() {
    CvApp()
}

/** Превью узкой раскладки: панели в колонку. */
@Preview(widthDp = 480, heightDp = 900)
@Composable
private fun CvAppNarrowPreview() {
    CvApp()
}
