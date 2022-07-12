/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.hdmsh.core_ui_playback.searching.DatmusicSearchParams.BackendType
import com.hdmsh.core_ui_playback.searching.DatmusicSearchParams.BackendType.Companion.asBackendTypes
import com.hdmsh.core_ui_playback.searching.errors.ApiCaptchaError


@Parcelize
data class SearchFilter(
    val backends: BackendTypes = DefaultBackends
) : Parcelable {

    val hasAudios get() = backends.contains(BackendType.AUDIOS)
    val hasArtists get() = backends.contains(BackendType.ARTISTS)
    val hasAlbums get() = backends.contains(BackendType.ALBUMS)

    val hasMinerva get() = backends.contains(BackendType.MINERVA)
    val hasFlacs get() = backends.contains(BackendType.FLACS)

    val hasMinervaOnly get() = backends.size == 1 && backends.contains(BackendType.MINERVA)

    companion object {
        val DefaultBackends = setOf(BackendType.AUDIOS, BackendType.ARTISTS, BackendType.ALBUMS)

        fun from(backends: String?) = SearchFilter(backends?.asBackendTypes() ?: DefaultBackends)
    }
}

data class SearchViewState(
    val filter: SearchFilter = SearchFilter(),
    val error: Throwable? = null,
    val captchaError: ApiCaptchaError? = null,
) {
    companion object {
        val Empty = SearchViewState()
    }
}

@Parcelize
data class SearchTrigger(val query: String = "", val captchaSolution: CaptchaSolution? = null) : Parcelable

data class SearchEvent(val searchTrigger: SearchTrigger, val searchFilter: SearchFilter)
