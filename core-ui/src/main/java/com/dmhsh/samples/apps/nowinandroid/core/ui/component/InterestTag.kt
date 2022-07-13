package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@Stable
interface TagColors {
    @Composable
    fun backgroundColor(enabled: Boolean): State<Color>

    @Composable
    fun contentColor(enabled: Boolean): State<Color>
}

@Immutable
private class DefaultTagColors(
    private val backgroundColor: Color,
    private val contentColor: Color
) : TagColors {
    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(newValue = backgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(newValue = contentColor)
    }
}

object TagDefaults {
    @Composable
    fun tagColors(
        backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = .2f),
        contentColor: Color = MaterialTheme.colorScheme.primary
    ): TagColors = DefaultTagColors(backgroundColor = backgroundColor, contentColor = contentColor)
}


object ItemTagDefaults {
    @Composable
    fun tagColors(
        backgroundColor: Color = androidx.compose.material.MaterialTheme.colors.background,
        contentColor: Color = androidx.compose.material.MaterialTheme.colors.onSurface
    ): TagColors = DefaultTagColors(backgroundColor = backgroundColor, contentColor = contentColor)
}

@Composable
fun InterestTag(
    text: String,
    modifier: Modifier = Modifier,
    colors: TagColors = ItemTagDefaults.tagColors(),
    shape: Shape = RoundedCornerShape(4.dp),
    style: TextStyle = typography.body1,
    onClick: () -> Unit = {}
) {
    val tagModifier = modifier
        .padding(4.dp)
        .clickable(onClick = onClick)
        .clip(shape = shape)
        .background(androidx.compose.material.MaterialTheme.colors.background)
        .padding(horizontal = 8.dp, vertical = 4.dp)
    Text(
        text = text,
        color = androidx.compose.material.MaterialTheme.colors.onSurface,
        modifier = tagModifier,
        style = style
    )
}
