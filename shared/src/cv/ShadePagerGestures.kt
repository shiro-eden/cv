package org.example.cv

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.pager.PagerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * Жесты «шторки» для HorizontalPager:
 * - тачпад и колесо мыши едут вместе с жестом, после паузы страница доводится
 *   до ближайшей;
 * - палец и мышь следуют за перетаскиванием, резкий свайп листает ровно на
 *   одну страницу — перелететь нельзя.
 */
fun Modifier.shadePagerGestures(
    pagerState: PagerState,
    scope: CoroutineScope,
): Modifier = this
    .pointerInput(pagerState) {
        var settleJob: Job? = null
        awaitPointerEventScope {
            while (true) {
                val event = awaitPointerEvent()
                if (event.type != PointerEventType.Scroll) continue
                val deltaX = event.changes.firstOrNull()?.scrollDelta?.x ?: 0f
                if (deltaX == 0f) continue
                event.changes.forEach { it.consume() }
                pagerState.dispatchRawDelta(deltaX)
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
    .pointerInput(pagerState) {
        var tracker: VelocityTracker? = null
        val flingThresholdPx = 50.dp.toPx()
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
