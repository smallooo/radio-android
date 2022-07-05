/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package com.dmhsh.samples.apps.nowinandroid.di

import android.app.Application
import android.content.Context
import com.dmhsh.samples.apps.nowinandroid.utils.AppHeadersInterceptor

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    val API_TIMEOUT = Duration.ofSeconds(40).toMillis()
    val DOWNLOADER_TIMEOUT = Duration.ofMinutes(3).toMillis()
    val PLAYER_TIMEOUT = Duration.ofMinutes(2).toMillis()
    val PLAYER_TIMEOUT_CONNECT = Duration.ofSeconds(30).toMillis()

    private fun getBaseBuilder(cache: Cache): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .cache(cache)
            .readTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .writeTimeout(API_TIMEOUT, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
    }

    @Provides
    @Singleton
    fun okHttpCache(app: Application) = Cache(app.cacheDir, (10 * 1024 * 1024).toLong())

    @Provides
    @Singleton
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        return interceptor
    }
//
    @Provides
    @Singleton
    @Named("AppHeadersInterceptor")
    fun appHeadersInterceptor(@ApplicationContext context: Context): Interceptor = AppHeadersInterceptor(context)
//
//    @Provides
//    @Singleton
//    @Named("RewriteCachesInterceptor")
//    fun rewriteCachesInterceptor(): Interceptor = RewriteCachesInterceptor()
//
//    @Provides
//    @Singleton
//    fun okHttp(
//        cache: Cache,
//        loggingInterceptor: HttpLoggingInterceptor,
//        @Named("AppHeadersInterceptor") appHeadersInterceptor: Interceptor,
//        @Named("RewriteCachesInterceptor") rewriteCachesInterceptor: Interceptor
//    ) = getBaseBuilder(cache)
//        .addInterceptor(appHeadersInterceptor)
//        .addInterceptor(rewriteCachesInterceptor)
//        .addInterceptor(loggingInterceptor)
//        .build()
//
//    @Provides
//    @Named("downloader")
//    fun downloaderOkHttp(
//        cache: Cache,
//        @Named("AppHeadersInterceptor") appHeadersInterceptor: Interceptor,
//    ) = getBaseBuilder(cache)
//        .readTimeout(Config.DOWNLOADER_TIMEOUT, TimeUnit.MILLISECONDS)
//        .writeTimeout(Config.DOWNLOADER_TIMEOUT, TimeUnit.MILLISECONDS)
//        .addInterceptor(appHeadersInterceptor)
//        .addInterceptor(HttpLoggingInterceptor().apply { level = LogLevel.HEADERS })
//        .build()

    @Provides
    @Named("player")
    fun playerOkHttp(
        cache: Cache,
        @Named("AppHeadersInterceptor") appHeadersInterceptor: Interceptor,
    ) = getBaseBuilder(cache)
        .readTimeout(PLAYER_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(PLAYER_TIMEOUT, TimeUnit.MILLISECONDS)
        .connectTimeout(PLAYER_TIMEOUT_CONNECT, TimeUnit.MILLISECONDS)
        .addInterceptor(appHeadersInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS })
        .build()

//    @Provides
//    @Singleton
//    fun jsonConfigured() = DEFAULT_JSON_FORMAT
//
    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun retrofit(client: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://at1.api.radio-browser.info/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BASIC)
                    }).build()
                    )
            .build()
}
