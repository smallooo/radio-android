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

package com.dmhsh.samples.apps.nowinandroid.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmhsh.samples.apps.nowinandroid.core.database.dao.*
import com.dmhsh.samples.apps.nowinandroid.core.database.model.*
import com.dmhsh.samples.apps.nowinandroid.core.database.util.InstantConverter
import com.dmhsh.samples.apps.nowinandroid.core.database.util.NewsResourceTypeConverter

@Database(
    entities = [
        AuthorEntity::class,
        EpisodeAuthorCrossRef::class,
        EpisodeEntity::class,
        NewsResourceAuthorCrossRef::class,
        NewsResourceEntity::class,
        NewsResourceTopicCrossRef::class,
        TopicEntity::class,
        StationEntity::class
    ],
    version = 10,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = DatabaseMigrations.Schema2to3::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
    ],
    exportSchema = true,
)
@TypeConverters(
    InstantConverter::class,
    NewsResourceTypeConverter::class,
)
abstract class NiADatabase : RoomDatabase() {
    abstract fun topicDao(): TopicDao
    abstract fun authorDao(): AuthorDao
    abstract fun episodeDao(): EpisodeDao
    abstract fun newsResourceDao(): NewsResourceDao
    abstract fun stationDao(): StationDao
}
