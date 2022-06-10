package com.hdmsh.core_ui_playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.app.nowinandroid.core.playback.SET_MEDIA_STATE
import com.google.samples.apps.nowinandroid.playback.PlaybackConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class PlaybackConnectionViewModel @Inject constructor(
    val playbackConnection: PlaybackConnection
) : ViewModel() {
    init {
        viewModelScope.launch {
            playbackConnection.isConnected.collectLatest { connected ->
                if (connected) {
                    //playbackConnection.transportControls?.sendCustomAction(SET_MEDIA_STATE, null)
                }
            }
        }
    }
}
