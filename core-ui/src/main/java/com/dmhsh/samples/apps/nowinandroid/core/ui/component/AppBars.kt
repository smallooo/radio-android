/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.core.ui.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppBarAlphas
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.topAppBarTitleStyle
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.topAppBarTitleStyleSmall
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.translucentSurfaceColor
import tm.alashow.i18n.R

val AppBarHeight = 56.dp
private val AppBarHorizontalPadding = 4.dp

private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)
private val TitleIconModifier = Modifier.width(72.dp - AppBarHorizontalPadding)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppTopBar(
    title: String,
    modifier: Modifier = Modifier,
    titleContent: @Composable () -> Unit = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
    collapsedProgress: Float = 1f,
    titleModifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    filterVisible: Boolean = false,
    filterContent: @Composable RowScope.() -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    val appBarColor = translucentSurfaceColor()
    val backgroundColor = appBarColor.copy(alpha = collapsedProgress.coerceAtMost(AppBarAlphas.translucentBarAlpha()))
    val contentColor = contentColorFor(backgroundColor)
    val titleStyle = if (navigationIcon != null) topAppBarTitleStyleSmall() else topAppBarTitleStyle()
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .statusBarsPadding()
                .padding(vertical = if (filterVisible || navigationIcon != null) 4.dp else 4.dp)
                .simpleClickable { },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                if (filterVisible) filterContent()
                else {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(5f)) {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                            if (navigationIcon == null) Spacer(TitleInsetWithoutIcon)
                            else Box(TitleIconModifier) { navigationIcon() }
                            Row(titleModifier.alpha(collapsedProgress)) {
                                ProvideTextStyle(titleStyle) {
                                    titleContent()
                                }
                            }
                        }
                    }
                    AppBarActionsRow(
                        actions = actions,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppBarActionsRow(
    modifier: Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit,
) {
    CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
            content = actions
        )
    }
}

@Composable
fun AppBarNavigationIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = stringResource(R.string.generic_back),
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            rememberVectorPainter(Icons.Filled.ArrowBack),
            contentDescription = contentDescription,
        )
    }
}
