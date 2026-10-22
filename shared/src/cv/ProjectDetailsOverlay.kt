package org.example.cv

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Оверлей «карточка проекта»: затемнение + карточка на большую часть экрана
 * с README проекта. Пружинно вырастает из плитки, закрывается крестиком
 * или кликом по затемнению.
 */
@Composable
fun ProjectDetailsOverlay(
    project: Project?,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Помним последний проект, чтобы карточка не схлопывалась во время анимации выхода
    var shown by remember { mutableStateOf(project) }
    if (project != null) shown = project
    val current = shown ?: return

    Box(modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = project != null,
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
            visible = project != null,
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
            ProjectDetailsCard(
                project = current,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun ProjectDetailsCard(
    project: Project,
    onDismiss: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 40.dp),
    ) {
        Surface(
            shape = RoundedCornerShape(36.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            modifier = Modifier.fillMaxSize(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = project.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "${project.year} · ${project.repo}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = CvIcons.Close,
                            contentDescription = "Закрыть",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                TechChipRow(techs = project.techs)

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                ) {
                    MarkdownText(project.readme)
                }

                FilledTonalButton(
                    onClick = { runCatching { uriHandler.openUri(project.githubUrl) } },
                    modifier = Modifier.align(Alignment.End),
                ) {
                    Icon(
                        imageVector = CvIcons.GitHub,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Открыть на GitHub")
                }
            }
        }
    }
}

@Preview(widthDp = 1280, heightDp = 800)
@Composable
private fun ProjectDetailsOverlayPreview() {
    CvTheme {
        ProjectDetailsOverlay(
            project = cvProfile.projects.first(),
            onDismiss = {},
        )
    }
}
