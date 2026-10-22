package org.example.cv

import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.example.cv.resources.Res
import org.example.cv.resources.avatar
import org.jetbrains.compose.resources.painterResource

/**
 * Фото профиля. Сейчас — заглушка `avatar.png`; просто замените этот файл в
 * `shared/composeResources/drawable/` своей фотографией с GitHub (или положите
 * файл с другим именем и поменяйте `Res.drawable.avatar` на новый аксессуар).
 */
@Composable
fun ProfilePhoto(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(Res.drawable.avatar),
        contentDescription = "Фотография профиля",
        contentScale = ContentScale.Crop,
        modifier = modifier.clip(CircleShape),
    )
}
