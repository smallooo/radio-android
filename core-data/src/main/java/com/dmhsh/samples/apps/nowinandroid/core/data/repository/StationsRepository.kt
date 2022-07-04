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

interface StationsRepository : Syncable {
    /**
     * Gets the available topics as a stream
     */
    fun getAllStationsStream(): Flow<List<Station>>


    fun getTopVisitedStationsStream(): Flow<List<Station>>

    fun gettopVotedStationsStream(): Flow<List<Station>>

    fun getLateUpdateStationsStream(): Flow<List<Station>>

    fun getnowPlayingStationsStream(): Flow<List<Station>>


    fun getTagList(): Flow<List<StationsTag>>



    fun getLanguageList(): Flow<List<LanguageTag>>

    fun getStationsByConditionList(): Flow<List<Station>>
//    /**
//     * Gets data for a specific topic
//     */
    fun getFavoriteStations(): Flow<List<Station>>

    fun setFavoriteStation(entitie: Station)

    fun setPlayHistory(entitie: Station)

    fun getPlayHistory(): Flow<List<Station>>

    fun getStationbyIdsEntitiesStream(entities: List<String>): Flow<List<Station>>
//
//    /**
//     * Sets the user's currently followed topics
//     */
//    suspend fun setFollowedStationIds(followedTopicIds: Set<String>)
//
//    /**
//     * Toggles the user's newly followed/unfollowed topic
//     */
//    suspend fun toggleFollowedStationId(followedTopicId: String, followed: Boolean)

    /**
     * Returns the users currently followed topics
     */
    fun getFollowedStationIdsStream(): Flow<Set<String>>
}