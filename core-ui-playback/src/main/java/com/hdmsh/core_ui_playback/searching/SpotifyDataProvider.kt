package com.hdmsh.core_ui_playback.searching

import androidx.compose.ui.graphics.Color


object SpotifyDataProvider {
    fun spotifySurfaceGradient(isDark: Boolean) =
        if (isDark) listOf(Color(0xFF2A2A2A), Color.Black) else listOf(Color.White, Color.LightGray)
}