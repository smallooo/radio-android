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

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dmhsh.samples.apps.nowinandroid.core.ui.component.ComingSoon

@Composable
fun SettingRoute(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    ComingSoon()
//    Setting(
//        onBackClick = onBackClick,
//        modifier = modifier,
//    )
}

@OptIn(ExperimentalFoundationApi::class)
@VisibleForTesting
@Composable
internal fun Setting(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


//        item {
//            Spacer(
//                // TODO: Replace with windowInsetsTopHeight after
//                //       https://issuetracker.google.com/issues/230383055
//                Modifier.windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
//                )
//            )
//        }
//
//        item {
//            Spacer(
//                // TODO: Replace with windowInsetsBottomHeight after
//                //       https://issuetracker.google.com/issues/230383055
//                Modifier.windowInsetsPadding(
//                    WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)
//                )
//            )
//        }
    }
}