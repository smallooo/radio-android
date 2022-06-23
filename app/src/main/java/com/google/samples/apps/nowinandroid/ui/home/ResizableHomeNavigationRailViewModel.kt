/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.ui.home

import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.lifecycle.SavedStateHandle
import com.google.samples.apps.nowinandroid.core.datastore.PreferencesStore

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


private val HomeNavigationRailDragOffsetKey = floatPreferencesKey("HomeNavigationRailWeightKey")

@HiltViewModel
class ResizableHomeNavigationRailViewModel @Inject constructor(
    handle: SavedStateHandle,
    preferencesStore: PreferencesStore,
) : ResizableLayoutViewModel(
    preferencesStore = preferencesStore,

    preferenceKey = HomeNavigationRailDragOffsetKey,
    defaultDragOffset = 0f,
    analyticsPrefix = "home.navigationRail"
)
