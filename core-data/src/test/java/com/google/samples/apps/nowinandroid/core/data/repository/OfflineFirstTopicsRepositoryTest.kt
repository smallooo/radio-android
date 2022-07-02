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

import com.dmhsh.samples.apps.nowinandroid.core.data.Synchronizer
import com.dmhsh.samples.apps.nowinandroid.core.data.model.asEntity

import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class OfflineFirstTopicsRepositoryTest {

   // private lateinit var subject: OfflineFirstTopicsRepository

//    private lateinit var topicDao: TopicDao
//
//    private lateinit var network: TestNiaNetwork
//
//    private lateinit var niaPreferences: NiaPreferences
//
//    private lateinit var synchronizer: Synchronizer
//
//    @get:Rule
//    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()
//
//    @Before
//    fun setup() {
//        topicDao = TestTopicDao()
//        network = TestNiaNetwork()
//        niaPreferences = NiaPreferences(
//            tmpFolder.testUserPreferencesDataStore()
//        )
//        synchronizer = TestSynchronizer(niaPreferences)
//
////        subject = OfflineFirstTopicsRepository(
////            topicDao = topicDao,
////            network = network,
////            niaPreferences = niaPreferences
////        )
//    }
//
//    @Test
//    fun offlineFirstTopicsRepository_topics_stream_is_backed_by_topics_dao() =
//        runTest {
//            Assert.assertEquals(
//                topicDao.getTopicEntitiesStream()
//                    .first()
//                    .map(TopicEntity::asExternalModel),
//                subject.getTopicsStream()
//                    .first()
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_news_resources_for_interests_is_backed_by_news_resource_dao() =
//        runTest {
//            Assert.assertEquals(
//                niaPreferences.followedTopicIds
//                    .first(),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_sync_pulls_from_network() =
//        runTest {
//            subject.syncWith(synchronizer)
//
//            val networkTopics = network.getTopics()
//                .map(NetworkTopic::asEntity)
//
//            val dbTopics = topicDao.getTopicEntitiesStream()
//                .first()
//
//            Assert.assertEquals(
//                networkTopics.map(TopicEntity::id),
//                dbTopics.map(TopicEntity::id)
//            )
//
//            // After sync version should be updated
//            Assert.assertEquals(
//                network.latestChangeListVersion(CollectionType.Topics),
//                synchronizer.getChangeListVersions().topicVersion
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_incremental_sync_pulls_from_network() =
//        runTest {
//            // Set topics version to 10
//            synchronizer.updateChangeListVersions {
//                copy(topicVersion = 10)
//            }
//
//            subject.syncWith(synchronizer)
//
//            val networkTopics = network.getTopics()
//                .map(NetworkTopic::asEntity)
//                // Drop 10 to simulate the first 10 items being unchanged
//                .drop(10)
//
//            val dbTopics = topicDao.getTopicEntitiesStream()
//                .first()
//
//            Assert.assertEquals(
//                networkTopics.map(TopicEntity::id),
//                dbTopics.map(TopicEntity::id)
//            )
//
//            // After sync version should be updated
//            Assert.assertEquals(
//                network.latestChangeListVersion(CollectionType.Topics),
//                synchronizer.getChangeListVersions().topicVersion
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_sync_deletes_items_marked_deleted_on_network() =
//        runTest {
//            val networkTopics = network.getTopics()
//                .map(NetworkTopic::asEntity)
//                .map(TopicEntity::asExternalModel)
//
//            // Delete half of the items on the network
//            val deletedItems = networkTopics
//                .map(Topic::id)
//                .partition { it.chars().sum() % 2 == 0 }
//                .first
//                .toSet()
//
//            deletedItems.forEach {
//                network.editCollection(
//                    collectionType = CollectionType.Topics,
//                    id = it,
//                    isDelete = true
//                )
//            }
//
//            subject.syncWith(synchronizer)
//
//            val dbTopics = topicDao.getTopicEntitiesStream()
//                .first()
//                .map(TopicEntity::asExternalModel)
//
//            // Assert that items marked deleted on the network have been deleted locally
//            Assert.assertEquals(
//                networkTopics.map(Topic::id) - deletedItems,
//                dbTopics.map(Topic::id)
//            )
//
//            // After sync version should be updated
//            Assert.assertEquals(
//                network.latestChangeListVersion(CollectionType.Topics),
//                synchronizer.getChangeListVersions().topicVersion
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_toggle_followed_topics_logic_delegates_to_nia_preferences() =
//        runTest {
//            subject.toggleFollowedTopicId(followedTopicId = "0", followed = true)
//
//            Assert.assertEquals(
//                setOf("0"),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//
//            subject.toggleFollowedTopicId(followedTopicId = "1", followed = true)
//
//            Assert.assertEquals(
//                setOf("0", "1"),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//
//            Assert.assertEquals(
//                niaPreferences.followedTopicIds
//                    .first(),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//        }
//
//    @Test
//    fun offlineFirstTopicsRepository_set_followed_topics_logic_delegates_to_nia_preferences() =
//        runTest {
//            subject.setFollowedTopicIds(followedTopicIds = setOf("1", "2"))
//
//            Assert.assertEquals(
//                setOf("1", "2"),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//
//            Assert.assertEquals(
//                niaPreferences.followedTopicIds
//                    .first(),
//                subject.getFollowedTopicIdsStream()
//                    .first()
//            )
//        }
}
