/*
 * Copyright (C) 2020, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.utils

import android.content.Context
import java.util.Locale
import okhttp3.Interceptor
import okhttp3.Response


internal class AppHeadersInterceptor(context: Context) : Interceptor {
    private val clientId = "context.androidId()"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("User-Agent", "Datmusic App/BuildConfig.VERSION_NAME}-BuildConfig.VERSION_CODE}")
            .header("Accept-Language", Locale.getDefault().language)
            .run {
                val host = chain.request().url.host
                when (host.contains("http://at1.api.radio-browser.info/")) {
                    true -> this.header("X-Datmusic-Id", clientId).header("X-Datmusic-Version", "APP_USER_AGENT")
                    else -> this
                }
            }
            .build()
        return chain.proceed(request)
    }
}
