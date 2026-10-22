package org.example.cv

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun App() {
    CvApp(modifier = Modifier.safeContentPadding().fillMaxSize())
}
