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

package com.google.samples.apps.nowinandroid.core.data.repository

import com.google.samples.apps.nowinandroid.core.data.Synchronizer
import com.google.samples.apps.nowinandroid.core.data.changeListSync
import com.google.samples.apps.nowinandroid.core.data.model.asEntity
import com.google.samples.apps.nowinandroid.core.database.dao.StationDao
import com.google.samples.apps.nowinandroid.core.database.dao.TopicDao
import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.model.data.Topic
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkTopic
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Disk storage backed implementation of the [TopicsRepository].
 * Reads are exclusively from local storage to support offline access.
 */
class OfflineFirstStationsRepository @Inject constructor(
    private val sationDao: StationDao,
    private val network: NiANetwork,
    private val niaPreferences: NiaPreferences,
) : StationsRepository {
    override fun getStationsStream(): Flow<List<Station>> =
       sationDao.getStationEntitiesStream().map { it.map(StationEntity::asExternalModel) }


    override fun getStation(id: String): Flow<Station> {
        TODO("Not yet implemented")
    }

    override suspend fun setFollowedStationIds(followedTopicIds: Set<String>) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleFollowedStationId(followedTopicId: String, followed: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getFollowedStationIdsStream(): Flow<Set<String>> =
        niaPreferences.followedAuthorIds

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean {
        TODO("Not yet implemented")
    }

    //    override fun getTopicsStream(): Flow<List<Topic>> =
//        topicDao.getTopicEntitiesStream()
//            .map { it.map(TopicEntity::asExternalModel) }
//
//    override fun getTopic(id: String): Flow<Topic> =
//        topicDao.getTopicEntity(id).map { it.asExternalModel() }
//
//    override suspend fun setFollowedTopicIds(followedTopicIds: Set<String>) =
//        niaPreferences.setFollowedTopicIds(followedTopicIds)
//
//    override suspend fun toggleFollowedTopicId(followedTopicId: String, followed: Boolean) =
//        niaPreferences.toggleFollowedTopicId(followedTopicId, followed)
//
//    override fun getFollowedTopicIdsStream() = niaPreferences.followedTopicIds
//
//    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
//        synchronizer.changeListSync(
//            versionReader = ChangeListVersions::topicVersion,
//            changeListFetcher = { currentVersion ->
//                network.getTopicChangeList(after = currentVersion)
//            },
//            versionUpdater = { latestVersion ->
//                copy(topicVersion = latestVersion)
//            },
//            modelDeleter = topicDao::deleteTopics,
//            modelUpdater = { changedIds ->
//                val networkTopics = network.getTopics(ids = changedIds)
//                topicDao.upsertTopics(
//                    entities = networkTopics.map(NetworkTopic::asEntity)
//                )
//            }
//        )
}
