package org.example.cv

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Левая панель профиля: фото, имя, о себе, образование, навыки, контакты.
 * Занимает примерно 1/4 — 1/5 ширины экрана (ширину задаёт вызов через
 * `Modifier.width(...)`).
 */
@Composable
fun ProfilePanel(profile: CvProfile, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxHeight(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            ProfileCard(profile)
            AboutSection(profile.about)
            EducationSection(profile.education)
            SkillsSection(profile.skills)
            ContactsSection(profile.contacts)
        }
    }
}

/** Фото в кольце + имя и роль по центру. */
@Composable
private fun ProfileCard(profile: CvProfile) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(176.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .border(4.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .padding(6.dp),
        ) {
            ProfilePhoto(Modifier.fillMaxSize())
        }
        Spacer(Modifier.size(16.dp))
        Text(
            text = profile.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Text(
            text = profile.role,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun AboutSection(about: String) {
    Sectioned(Modifier.fillMaxWidth(), header = "О себе") {
        Text(
            text = about,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun EducationSection(entries: List<EducationEntry>) {
    Sectioned(Modifier.fillMaxWidth(), header = "Образование") {
        entries.forEach { entry ->
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = entry.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SkillsSection(skills: List<String>) {
    Sectioned(Modifier.fillMaxWidth(), header = "Навыки") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            skills.forEach { skill -> SkillChip(skill) }
        }
    }
}

/** Небольшая «пилюля» навыка в secondary-контейнере. */
@Composable
private fun SkillChip(label: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun ContactsSection(contacts: List<Contact>) {
    val uriHandler = LocalUriHandler.current
    Sectioned(Modifier.fillMaxWidth(), header = "Контакты") {
        contacts.forEach { contact ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { runCatching { uriHandler.openUri(contact.url) } }
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Icon(
                    imageVector = contact.type.icon(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = contact.value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}

fun ContactType.icon(): ImageVector = when (this) {
    ContactType.EMAIL -> Icons.Filled.Email
    ContactType.TELEGRAM -> CvIcons.Telegram
    ContactType.GITHUB -> CvIcons.GitHub
}

/** Секция: заголовок + содержимое с небольшим отступом. */
@Composable
private fun Sectioned(
    modifier: Modifier = Modifier,
    header: String,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionHeader(header)
        content()
    }
}
