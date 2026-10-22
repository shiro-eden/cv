package org.example.cv

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Жесты "шторки" для пейджера (горизонтального или вертикального):
 * - колесо и тачпад едут вместе с жестом — по основной оси пейджера, а при
 *   пустой основной оси (вертикальный скролл двумя пальцами) по запасной;
 *   после паузы страница доводится до ближайшей;
 * - палец и мышь следуют за перетаскиванием, резкий свайп листает ровно на
 *   одну страницу — перелететь нельзя.
 */
fun Modifier.shadePagerGestures(
    pagerState: PagerState,
    scope: CoroutineScope,
    vertical: Boolean = false,
): Modifier = this
    .pointerInput(pagerState, vertical) {
        var settleJob: Job? = null
        awaitPointerEventScope {
            while (true) {
                // Initial-пасс: забираем колесо/тачпад раньше родителей
                // (в узкой раскладке иначе его съедает внешний скролл).
                val event = awaitPointerEvent(PointerEventPass.Initial)
                if (event.type != PointerEventType.Scroll) continue
                val scroll = event.changes.firstOrNull()?.scrollDelta ?: continue
                // Основная ось пейджера; тачпад чаще шлёт скролл по вертикали,
                // поэтому при пустой основной оси берём запасную.
                val primary = if (vertical) scroll.y else scroll.x
                val fallback = if (vertical) scroll.x else scroll.y
                val delta = if (primary != 0f) primary else fallback
                if (delta == 0f) continue
                event.changes.forEach { it.consume() }
                pagerState.dispatchRawDelta(delta)
                settleJob?.cancel()
                settleJob = scope.launch {
                    delay(150)
                    val nearest = pagerState.currentPage -
                        pagerState.currentPageOffsetFraction.roundToInt()
                    pagerState.animateScrollToPage(
                        nearest.coerceIn(0, pagerState.pageCount - 1),
                    )
                }
            }
        }
    }
    .pointerInput(pagerState, vertical) {
        var tracker: VelocityTracker? = null
        val flingThresholdPx = 50.dp.toPx()
        if (vertical) {
            detectVerticalDragGestures(
                onDragStart = { tracker = VelocityTracker() },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    tracker?.addPosition(change.uptimeMillis, change.position)
                    scope.launch { pagerState.dispatchRawDelta(-dragAmount) }
                },
                onDragEnd = {
                    val dragTracker = tracker
                    tracker = null
                    scope.launch {
                        val velocityY = dragTracker?.calculateVelocity()?.y ?: 0f
                        val target = when {
                            velocityY < -flingThresholdPx -> pagerState.currentPage + 1
                            velocityY > flingThresholdPx -> pagerState.currentPage - 1
                            else -> pagerState.currentPage -
                                pagerState.currentPageOffsetFraction.roundToInt()
                        }
                        pagerState.animateScrollToPage(
                            target.coerceIn(0, pagerState.pageCount - 1),
                        )
                    }
                },
            )
        } else {
            detectHorizontalDragGestures(
                onDragStart = { tracker = VelocityTracker() },
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    tracker?.addPosition(change.uptimeMillis, change.position)
                    scope.launch { pagerState.dispatchRawDelta(-dragAmount) }
                },
                onDragEnd = {
                    val dragTracker = tracker
                    tracker = null
                    scope.launch {
                        val velocityX = dragTracker?.calculateVelocity()?.x ?: 0f
                        val target = when {
                            velocityX < -flingThresholdPx -> pagerState.currentPage + 1
                            velocityX > flingThresholdPx -> pagerState.currentPage - 1
                            else -> pagerState.currentPage -
                                pagerState.currentPageOffsetFraction.roundToInt()
                        }
                        pagerState.animateScrollToPage(
                            target.coerceIn(0, pagerState.pageCount - 1),
                        )
                    }
                },
            )
        }
    }
