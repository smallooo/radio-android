/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.apps.nowinandroid.core.network.retrofit

import com.google.samples.apps.nowinandroid.core.network.BuildConfig
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API declaration for NIA Network API
 */
private interface RetrofitNiANetworkApi {
    @GET(value = "topics")
    suspend fun getTopics(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkTopic>>


    @GET(value = "/json/stations/bycountry/japan")
    suspend fun getStations(): NetworkResponse<List<NetworkStation>>

    @GET(value = "json/countries")
    suspend fun getCountries(): NetworkResponse<List<NetworkCountry>>

    @GET(value = "authors")
    suspend fun getAuthors(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkAuthor>>

    @GET(value = "newsresources")
    suspend fun getNewsResources(
        @Query("id") ids: List<String>?,
    ): NetworkResponse<List<NetworkNewsResource>>

    @GET(value = "changelists/topics")
    suspend fun getTopicChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/authors")
    suspend fun getAuthorsChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

    @GET(value = "changelists/newsresources")
    suspend fun getNewsResourcesChangeList(
        @Query("after") after: Int?,
    ): List<NetworkChangeList>

}

private const val NiABaseUrl = BuildConfig.BACKEND_URL

/**
 * Wrapper for data provided from the [NiABaseUrl]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T
)

/**
 * [Retrofit] backed [NiANetwork]
 */
@Singleton
class RetrofitNiANetwork @Inject constructor(
    networkJson: Json
) : NiANetwork {

    private val networkApi = Retrofit.Builder()
        .baseUrl(NiABaseUrl)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(
                    // TODO: Decide logging logic
                    HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                )
                .build()
        )
        .addConverterFactory(networkJson.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitNiANetworkApi::class.java)

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        networkApi.getTopics(ids = ids).data

    override suspend fun getStations(): List<NetworkStation> =
        networkApi.getStations().data

    override suspend fun getCountries(): List<NetworkCountry> =
        networkApi.getCountries().data

    override suspend fun getAuthors(ids: List<String>?): List<NetworkAuthor> =
        networkApi.getAuthors(ids = ids).data

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        networkApi.getNewsResources(ids = ids).data

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getTopicChangeList(after = after)

    override suspend fun getAuthorChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getAuthorsChangeList(after = after)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        networkApi.getNewsResourcesChangeList(after = after)
}
