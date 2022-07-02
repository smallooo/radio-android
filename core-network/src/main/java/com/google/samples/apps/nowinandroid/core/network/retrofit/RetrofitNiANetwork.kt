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

package com.dmhsh.samples.apps.nowinandroid.core.network.retrofit

import com.dmhsh.samples.apps.nowinandroid.core.network.BuildConfig
import com.dmhsh.samples.apps.nowinandroid.core.network.Dispatcher
import com.dmhsh.samples.apps.nowinandroid.core.network.NiANetwork
import com.dmhsh.samples.apps.nowinandroid.core.network.NiaDispatchers
import com.dmhsh.samples.apps.nowinandroid.core.network.fake.FakeDataSource

import com.dmhsh.samples.apps.nowinandroid.core.network.model.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
    @Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
    private val networkJson: Json
) : NiANetwork {

    private val networkApi = Retrofit.Builder()
        .baseUrl("https://at1.api.radio-browser.info/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

        .client(OkHttpClient())
        .build()

    override suspend fun getTopics(ids: List<String>?): List<NetworkTopic> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.topicsData)
        }

    override suspend fun getStations(): List<NetworkStation> {
        TODO("Not yet implemented")
    }

    override suspend fun getCountries(): List<NetworkCountry> {
        TODO("Not yet implemented")
    }

    override suspend fun getNewsResources(ids: List<String>?): List<NetworkNewsResource> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.data)
        }

    override suspend fun getAuthors(ids: List<String>?): List<NetworkAuthor> =
        withContext(ioDispatcher) {
            networkJson.decodeFromString(FakeDataSource.authors)
        }

    override suspend fun getTopicChangeList(after: Int?): List<NetworkChangeList> =
        getTopics().mapToChangeList(NetworkTopic::id)

    override suspend fun getAuthorChangeList(after: Int?): List<NetworkChangeList> =
        getAuthors().mapToChangeList(NetworkAuthor::id)

    override suspend fun getNewsResourceChangeList(after: Int?): List<NetworkChangeList> =
        getNewsResources().mapToChangeList(NetworkNewsResource::id)
}

private fun <T> List<T>.mapToChangeList(
    idGetter: (T) -> String
) = mapIndexed { index, item ->
    NetworkChangeList(
        id = idGetter(item),
        changeListVersion = index,
        isDelete = false,
    )
}