package org.example.cv

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * Точки-страницы: активная вытягивается в «пилюлю», как в шторке Android.
 * Горизонтальная лента под пейджером или вертикальная ([vertical]) сбоку —
 * чтобы направление индикатора совпадало с направлением перелистывания.
 */
@Composable
fun PageDots(
    page: Int,
    pageCount: Int,
    onPageChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
) {
    @Composable
    fun Dot(index: Int) {
        val active = index == page
        val long by animateDpAsState(
            targetValue = if (active) 24.dp else 8.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow,
            ),
            label = "dotLong",
        )
        val dotColor by animateColorAsState(
            targetValue = if (active) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            },
            animationSpec = tween(250),
            label = "dotColor",
        )
        Box(
            modifier = Modifier
                .padding(horizontal = if (vertical) 3.dp else 0.dp, vertical = if (vertical) 0.dp else 3.dp)
                .size(
                    width = if (vertical) 8.dp else long,
                    height = if (vertical) long else 8.dp,
                )
                .clip(RoundedCornerShape(50))
                .background(dotColor)
                .clickable { onPageChange(index) },
        )
    }

    if (vertical) {
        Column(
            modifier = modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            repeat(pageCount) { index -> Dot(index) }
        }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            repeat(pageCount) { index -> Dot(index) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PageDotsPreview() {
    CvTheme {
        Surface(Modifier.padding(8.dp)) {
            PageDots(
                page = 1,
                pageCount = 3,
                onPageChange = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
