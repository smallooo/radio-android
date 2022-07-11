/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching.errors

import androidx.annotation.StringRes
import com.hdmsh.core_ui_playback.R
import com.hdmsh.core_ui_playback.searching.ApiResponse

import java.time.Instant

open class ApiErrorException(
    open val error: ApiResponse.Error = ApiResponse.Error(),

    @StringRes
    open val errorRes: Int? = null
) : RuntimeException("API returned an error: id = ${error.id}, message = ${error.message}") {
    override fun toString() = message ?: super.toString()
}

data class ApiNotFoundError(override val error: ApiResponse.Error = ApiResponse.Error("notFound")) : ApiErrorException(error, R.string.error_no_equalizer_found)
data class ApiCaptchaError(override val error: ApiResponse.Error, val expiresAt: Instant = Instant.now().plusSeconds(30)) :
    ApiErrorException(error, R.string.error_empty_field) {
    val expired get() = Instant.now().isAfter(expiresAt)
}

fun ApiErrorException.mapToApiError(): ApiErrorException = when (error.id) {
    "notFound" -> ApiNotFoundError(error)
    "captcha" -> ApiCaptchaError(error)
    else -> this
}

fun apiError(id: String = "unknown", message: String? = null) = ApiErrorException(ApiResponse.Error(id, message))
