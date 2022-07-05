/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.feature.author.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.AppTheme
import com.dmhsh.samples.apps.nowinandroid.core.util.IntentUtils


@Composable
internal fun SettingsSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text, style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.secondary,
        modifier = modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 8.dp))
    )
}

@Composable
internal fun SettingsLinkItem(
    @StringRes labelRes: Int,
    @StringRes textRes: Int,
    @StringRes linkRes: Int,
) {
    SettingsLinkItem(stringResource(labelRes), stringResource(textRes), stringResource(linkRes))
}

@Composable
internal fun SettingsLinkItem(
    label: String,
    text: String,
    link: String,
   // analytics: FirebaseAnalytics = LocalAnalytics.current
) {
    SettingsItem(label, verticalAlignment = Alignment.Top) {
        val context = LocalContext.current
        ClickableText(
            text = buildAnnotatedString { append(text) },
            style = TextStyle.Default.copy(
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.End
            ),
            onClick = {
              //  analytics.event("settings.linkClick", mapOf("link" to link))
                IntentUtils.openUrl(context, link.toUri())
            }
        )
    }
}

@Composable
internal fun SettingsItem(
    label: String,
    modifier: Modifier = Modifier,
    labelModifier: Modifier = Modifier,
    labelWeight: Float = 1f,
    contentWeight: Float = 1f,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
    ) {
        Text(
            label,
            style = MaterialTheme.typography.subtitle1,
            modifier = labelModifier
                .padding(end = 4.dp)
                .weight(labelWeight)
        )
        Box(
            modifier = Modifier.weight(contentWeight, false),
            contentAlignment = Alignment.CenterEnd
        ) { content() }
    }
}

//@Composable
//internal fun SettingsLoadingButton(
//    isLoading: Boolean,
//    text: String,
//    modifier: Modifier = Modifier,
//    enabled: Boolean = true,
//    colors: ButtonColors = outlinedButtonColors(),
//    onClick: Callback,
//) {
//    OutlinedButton(
//        onClick = onClick,
//        enabled = enabled && !isLoading,
//        colors = colors,
//        modifier = modifier,
//    ) {
//        if (isLoading)
//            ProgressIndicatorSmall(Modifier.padding(end = AppTheme.specs.paddingSmall))
//        Text(text, maxLines = 1)
//    }
//}
