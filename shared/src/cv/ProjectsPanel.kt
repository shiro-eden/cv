package org.example.cv

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlinx.coroutines.launch

/** Плиток на странице широкой раскладки: сетка 2×2. */
private const val WIDE_TILES_PER_PAGE = 4

/**
 * Правая панель — «шторка» проектов в духе Android: ряд фильтр-чипов по
 * технологиям, крупные скруглённые плитки, перелистывание свайпом/колесом,
 * точки-страницы внизу и шестерёнка настроек в шапке. На широком экране —
 * сетка 2×2 с горизонтальным перелистыванием, на узком ([tilesPerPage] = 1) —
 * одна плитка на страницу и вертикальное перелистывание: страница уходит
 * вверх, следующая приходит снизу.
 */
@Composable
fun ProjectsPanel(
    projects: List<Project>,
    onProjectClick: (Project) -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    tilesPerPage: Int = WIDE_TILES_PER_PAGE,
) {
    var techFilter by remember { mutableStateOf<Tech?>(null) }
    val visibleProjects = remember(projects, techFilter) {
        if (techFilter == null) projects else projects.filter { techFilter in it.techs }
    }
    val allTechs = remember(projects) { projects.flatMap { it.techs }.distinct() }
    val singleTile = tilesPerPage == 1
    val pageCount = ceil(visibleProjects.size / tilesPerPage.toDouble()).toInt().coerceAtLeast(1)
    val pagerState = rememberPagerState(pageCount = { pageCount })
    val scope = rememberCoroutineScope()
    // после смены фильтра страниц может стать меньше — возвращаемся на первую
    LaunchedEffect(techFilter) { pagerState.scrollToPage(0) }

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
                count = visibleProjects.size,
                onOpenSettings = onOpenSettings,
            )
            TechFilterRow(
                techs = allTechs,
                selected = techFilter,
                onSelect = { techFilter = it },
            )

            if (singleTile) {
                Row(Modifier.weight(1f).fillMaxWidth()) {
                    VerticalPager(
                        state = pagerState,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .shadePagerGestures(pagerState, scope, vertical = true),
                        pageSpacing = 20.dp,
                        userScrollEnabled = false,
                    ) { pageIndex ->
                        ProjectsPage(
                            projects = visibleProjects,
                            pageIndex = pageIndex,
                            tilesPerPage = tilesPerPage,
                            onProjectClick = onProjectClick,
                        )
                    }
                    // Лента точек сбоку: индикатор движется вверх/вниз вместе
                    // со страницами, а не вразрез с ними.
                    PageDots(
                        page = pagerState.currentPage,
                        pageCount = pageCount,
                        onPageChange = { index ->
                            scope.launch { pagerState.animateScrollToPage(index) }
                        },
                        modifier = Modifier.fillMaxHeight().padding(start = 10.dp),
                        vertical = true,
                    )
                }
            } else {
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
                        projects = visibleProjects,
                        pageIndex = pageIndex,
                        tilesPerPage = tilesPerPage,
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
                imageVector = CvIcons.Settings,
                contentDescription = "Настройки",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Ряд фильтр-чипов по технологиям: «Все» плюс по чипу на технологию. */
@Composable
private fun TechFilterRow(
    techs: List<Tech>,
    selected: Tech?,
    onSelect: (Tech?) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = selected == null,
            onClick = { onSelect(null) },
            label = { Text("Все") },
        )
        techs.forEach { tech ->
            FilterChip(
                selected = selected == tech,
                onClick = { onSelect(if (selected == tech) null else tech) },
                label = { Text(tech.label) },
            )
        }
    }
}

/** Одна страница пейджера: сетка 2×2 или одна плитка на весь лист. */
@Composable
private fun ProjectsPage(
    projects: List<Project>,
    pageIndex: Int,
    tilesPerPage: Int,
    onProjectClick: (Project) -> Unit,
) {
    if (tilesPerPage == 1) {
        ProjectTile(
            project = projects[pageIndex],
            onClick = { onProjectClick(projects[pageIndex]) },
            summaryMaxLines = Int.MAX_VALUE,
            modifier = Modifier.fillMaxSize(),
        )
        return
    }
    val pageProjects = projects.drop(pageIndex * tilesPerPage).take(tilesPerPage)
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

@Preview(widthDp = 800, heightDp = 800, showBackground = true)
@Composable
private fun ProjectsPanelPreview() {
    CvTheme {
        ProjectsPanel(
            projects = cvProfile.projects,
            onProjectClick = {},
            onOpenSettings = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
