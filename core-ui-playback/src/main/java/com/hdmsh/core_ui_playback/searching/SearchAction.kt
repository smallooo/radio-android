/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.hdmsh.core_ui_playback.searching.errors.ApiCaptchaError


sealed class SearchAction {
    data class QueryChange(val query: String = "") : SearchAction()
    object Search : SearchAction()
    data class SelectBackendType(val selected: Boolean, val backendType: DatmusicSearchParams.BackendType) : SearchAction()

    data class AddError(val error: Throwable) : SearchAction()
    object ClearError : SearchAction()
    data class SubmitCaptcha(val captchaError: ApiCaptchaError, val solution: String) : SearchAction()

    data class PlayRadio(val station: Station) : SearchAction()
}
