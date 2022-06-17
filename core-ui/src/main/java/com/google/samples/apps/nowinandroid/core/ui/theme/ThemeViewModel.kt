/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.google.samples.apps.nowinandroid.core.ui.theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


object PreferenceKeys {
    const val THEME_STATE_KEY = "theme_state"
}

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
   // private val preferences: PreferencesStore,
    //private val analytics: FirebaseAnalytics
) : ViewModel() {

    val themeState = DefaultTheme
//
//    fun applyThemeState(themeState: ThemeState) {
//       // analytics.event("theme.apply", mapOf("darkMode" to themeState.isDarkMode, "palette" to themeState.colorPalettePreference.name))
//        viewModelScope.launch {
//            preferences.save(PreferenceKeys.THEME_STATE_KEY, themeState, ThemeState.serializer())
//        }
//    }
}
