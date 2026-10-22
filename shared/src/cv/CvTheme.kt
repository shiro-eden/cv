package org.example.cv

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

/**
 * Углы чуть крупнее базовых — "Expressive": крупные панели скруглены на 32dp,
 * плитки и карточки берут из схемы свои уровни.
 */
private val CvShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(32.dp),
)

@Composable
fun CvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    palette: CvPalette = CvPalette.Purple,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = palette.scheme(darkTheme),
        shapes = CvShapes,
        content = content,
    )
}
