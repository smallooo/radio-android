/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import com.hdmsh.core_ui_playback.searching.errors.ApiErrorException
import com.hdmsh.core_ui_playback.searching.errors.mapToApiError
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class ApiResponse(
    @SerialName("status")
    val status: String,

    @SerialName("error")
    val error: Error? = null,

    @SerialName("data")
    val data: Data = Data(),
) {

    val isSuccessful get() = status == "ok"

    @Serializable
    data class Error(
        @SerialName("id")
        val id: String = "unknown",

        @SerialName("message")
        var message: String? = null,

        @SerialName("code")
        val code: Int = 0,

        @SerialName("captcha_id")
        val captchaId: Long = 0,

        @SerialName("captcha_img")
        val captchaImageUrl: String = "",

        @SerialName("captcha_index")
        val captchaIndex: Int = -1,
    )

    @Serializable
    data class Data(
        @SerialName("message")
        val message: String = "",


    )
}

fun ApiResponse.checkForErrors(): ApiResponse = if (isSuccessful) this
else throw ApiErrorException(error ?: ApiResponse.Error("unknown", "Unknown error"))
    .mapToApiError()
