/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.feature.author

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    handle: SavedStateHandle,
    //remoteConfig: RemoteConfig,
) : ViewModel() {

//    val settingsLinks = flow {
//        // initially fetch once then one more time when there might be an update
//        emit(remoteConfig.getSettingsLinks())
//        delay(REMOTE_CONFIG_FETCH_DELAY)
//        emit(remoteConfig.getSettingsLinks())
//    }.stateInDefault(viewModelScope, emptyList())
}
