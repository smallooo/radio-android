/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmhsh.samples.apps.nowinandroid.feature.author

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.AppTopBar
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.rememberFlowWithLifecycle
import com.dmhsh.samples.apps.nowinandroid.core.ui.theme.*
import com.dmhsh.samples.apps.nowinandroid.feature.author.ui.SelectableDropdownMenu
import com.dmhsh.samples.apps.nowinandroid.feature.author.ui.SettingsItem
import com.dmhsh.samples.apps.nowinandroid.feature.author.ui.SettingsLinkItem
import com.dmhsh.samples.apps.nowinandroid.feature.author.ui.SettingsSectionLabel
import tm.alashow.i18n.R

val LocalAppVersion = staticCompositionLocalOf { "Unknown" }

@Composable
fun SettingRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    themeViewModel: ThemeViewModel = hiltViewModel(),
    //viewModel: SettingsViewModel = hiltViewModel(),
) {

    //val themeState by rememberFlowWithLifecycle(themeViewModel.themeState)
    //val settingsLinks by rememberFlowWithLifecycle(viewModel.settingsLinks)
    Settings()
}

@Composable
private fun Settings(
  //  themeState: ThemeState,
  //  setThemeState: (ThemeState) -> Unit,
   // settingsLinks: SettingsLinks = emptyList()
) {
    Scaffold(
        topBar = {
            AppTopBar(title = "Settings")
        }
    ) { padding ->
        SettingsList(
          //  themeState,
         //   setThemeState,
//            settingsLinks,
            padding)
    }
}

@Composable
fun SettingsList(
   // themeState: ThemeState,
   // setThemeState: (ThemeState) -> Unit,
   // settingsLinks: SettingsLinks,
    paddings: PaddingValues,
   // downloader: Downloader = LocalDownloader.current
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = paddings
    ) {
       // settingsGeneralSection()
       // settingsThemeSection(themeState, setThemeState)
        //settingsDownloadsSection(downloader)
       // settingsDatabaseSection()
        settingsAboutSection()
        //settingsLinksSection(settingsLinks)
    }
}

fun LazyListScope.settingsGeneralSection() {
    item {
        SettingsSectionLabel(stringResource(R.string.app_name))

        SettingsItem(stringResource(R.string.app_name)) {
            //PremiumButton()
            Button(onClick = { /*TODO*/ }) { Text("PremiumButton")}
        }
    }
}

//fun LazyListScope.settingsDownloadsSection(downloader: Downloader) {
//    item {
//        val coroutine = rememberCoroutineScope()
//        val downloadsLocationSelected by rememberFlowWithLifecycle(downloader.hasDownloadsLocation).collectAsState(initial = null)
//        val downloadsSongsGrouping by rememberFlowWithLifecycle(downloader.downloadsSongsGrouping).collectAsState(initial = null)
//
//        SettingsSectionLabel(stringResource(R.string.settings_downloads))
//        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)) {
//            SettingsItem(stringResource(R.string.settings_downloads_location)) {
//                OutlinedButton(
//                    onClick = { downloader.requestNewDownloadsLocation() },
//                    colors = outlinedButtonColors()
//                ) {
//                    if (downloadsLocationSelected != null) {
//                        androidx.compose.material.Text(
//                            stringResource(
//                                if (downloadsLocationSelected == true) R.string.settings_downloads_location_change
//                                else R.string.settings_downloads_location_select
//                            )
//                        )
//                    }
//                }
//            }
//
//            SettingsItem(stringResource(R.string.settings_downloads_songsGrouping)) {
//                val downloadSongsGrouping = downloadsSongsGrouping ?: return@SettingsItem
//                SelectableDropdownMenu(
//                    items = DownloadsSongsGrouping.values().toList(),
//                    itemLabelMapper = { stringResource(it.labelRes) },
//                    subtitles = DownloadsSongsGrouping.values().map { stringResource(it.exampleRes) },
//                    selectedItem = downloadSongsGrouping,
//                    onItemSelect = { coroutine.launch { downloader.setDownloadsSongsGrouping(it) } },
//                    modifier = Modifier.offset(x = 12.dp)
//                )
//            }
//        }
//    }
//}

fun LazyListScope.settingsThemeSection(themeState: ThemeState, setThemeState: (ThemeState) -> Unit) {
    item {
        SettingsSectionLabel(stringResource(R.string.settings_theme_selection_title))
        SettingsItem(stringResource(R.string.settings_theme_dark_mode)) {
            SelectableDropdownMenu(
                items = DarkModePreference.values().toList(),
                selectedItem = themeState.darkModePreference,
                onItemSelect = {  setThemeState(themeState.copy(darkModePreference = it)) },
                modifier = Modifier.offset(x = 12.dp)
            )
        }
        SettingsItem(stringResource(R.string.settings_theme_colorPalette)) {
            SelectableDropdownMenu(
                items = ColorPalettePreference.values().toList(),
                selectedItem = themeState.colorPalettePreference,
                onItemSelect = { setThemeState(themeState.copy(colorPalettePreference = it))  },
                modifier = Modifier.offset(x = 12.dp)
            )
        }
    }
}

fun LazyListScope.settingsAboutSection() {
    item {
        SettingsSectionLabel(stringResource(R.string.nav_item_about))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SettingsLinkItem(
                labelRes = R.string.action_copy_info,
                textRes = R.string.action_countries,
                linkRes = R.string.web_page
            )
            SettingsLinkItem(
                label = stringResource(R.string.about_version),
                text = LocalAppVersion.current,
                link = "https://m.thebeastshop.com"
            )
        }

        Spacer(modifier = Modifier.height(160.dp))
    }
}

//fun LazyListScope.settingsLinksSection(settingsLinks: SettingsLinks) {
//    settingsLinks.forEach { settingsLink ->
//        item {
//            settingsLink.localizedCategory?.let { category ->
//                SettingsSectionLabel(category)
//            }
//
//            SettingsLinkItem(
//                label = settingsLink.localizedLabel,
//                text = settingsLink.getLinkName(),
//                link = settingsLink.getLinkUrl()
//            )
//        }
//    }
//}

internal fun LazyListScope.settingsDatabaseSection() {
    item {
        SettingsSectionLabel(stringResource(R.string.app_name))
        SettingsItem(stringResource(R.string.app_name)) {
            //BackupRestoreButton()
            Button(onClick = {}){
                Text("BackupRestoreButton")
            }
        }
    }
}