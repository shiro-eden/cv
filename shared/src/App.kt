package org.example.cv

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun App() {
    CvApp(modifier = Modifier.safeContentPadding().fillMaxSize())
}

@Preview(
    showBackground = true,
    widthDp = 1280,
    heightDp = 720
)
@Composable
private fun AppPreview() {
    App()
}
