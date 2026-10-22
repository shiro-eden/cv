package org.example.cv

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlinx.coroutines.launch

/** Плиток на странице: сетка 2×2. */
private const val TILES_PER_PAGE = 4

/**
 * Правая панель — «шторка» проектов в духе Android: крупные скруглённые плитки
 * по 4 на странице, перелистывание свайпом/колесом, точки-страницы внизу и
 * шестерёнка настроек в шапке.
 */
@Composable
fun ProjectsPanel(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pageCount = ceil(projects.size / TILES_PER_PAGE.toDouble()).toInt().coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()

    Surface(
        modifier = modifier.fillMaxHeight(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProjectsHeader(
                count = projects.size,
                onOpenSettings = onOpenSettings,
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .shadePagerGestures(pagerState, scope),
                pageSpacing = 20.dp,
                userScrollEnabled = false,
            ) { pageIndex ->
                ProjectsPage(
                    projects = projects,
                    pageIndex = pageIndex,
                    onProjectClick = onProjectClick,
                )
            }

            PageDots(
                page = pagerState.currentPage,
                pageCount = pageCount,
                onPageChange = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
            )
        }
    }
}

@Composable
private fun ProjectsHeader(
    count: Int,
    onOpenSettings: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = "Проекты",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            )
        }
        Spacer(Modifier.weight(1f))
        // Функционал настроек будет доделан отдельным запуском.
        IconButton(onClick = onOpenSettings) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Настройки",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Одна страница пейджера: до 4 плиток в сетке 2×2; пустые слоты сохраняют сетку. */
@Composable
private fun ProjectsPage(
    projects: List<Project>,
    pageIndex: Int,
    onProjectClick: (Project) -> Unit,
) {
    val pageProjects = projects.drop(pageIndex * TILES_PER_PAGE).take(TILES_PER_PAGE)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        repeat(2) { row ->
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                pageProjects.drop(row * 2).take(2).forEach { project ->
                    ProjectTile(
                        project = project,
                        onClick = { onProjectClick(project) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                    )
                }
                repeat(2 - pageProjects.drop(row * 2).take(2).size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
    }
}
