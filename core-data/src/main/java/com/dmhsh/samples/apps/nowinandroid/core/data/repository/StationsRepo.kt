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

package com.dmhsh.samples.apps.nowinandroid.core.data.repository

import com.dmhsh.samples.apps.nowinandroid.core.data.Syncable
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Topic
import kotlinx.coroutines.flow.Flow

interface StationsRepo : Syncable {

    fun getAllStream(): Flow<List<Station>>

    fun getTopVisitedStream(): Flow<List<Station>>

    fun gettopVotedStream(): Flow<List<Station>>

    fun getLateUpdateStream(): Flow<List<Station>>

    fun getnowPlayingStream(): Flow<List<Station>>

    fun getTagList(): Flow<List<StationsTag>>

    fun getLanguageList(): Flow<List<LanguageTag>>

    fun getSearchByTypeList(): Flow<List<Station>>

    fun getFavorite(): Flow<List<Station>>

    fun setFavorite(entitie: Station)

    fun setPlayHistory(entitie: Station)

    fun getPlayHistory(): Flow<List<Station>>

    fun getbyIdsEntitiesStream(entities: List<String>): Flow<List<Station>>

//    suspend fun setFollowedStationIds(followedTopicIds: Set<String>)

//    suspend fun toggleFollowedStationId(followedTopicId: String, followed: Boolean)

    fun getFollowedIdsStream(): Flow<Set<String>>
}
