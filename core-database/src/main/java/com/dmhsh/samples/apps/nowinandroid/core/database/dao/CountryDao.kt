package com.dmhsh.samples.apps.nowinandroid.core.database.dao

import androidx.room.*
import com.dmhsh.samples.apps.nowinandroid.core.database.model.CountryEntity
import com.dmhsh.samples.apps.nowinandroid.core.database.model.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query(
        value = """
        SELECT * FROM country
    """
    )
    fun getCountrysEntity(): Flow<ArrayList<CountryEntity>>


    /**
     * Inserts [topicEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreTopics(topicEntities: List<CountryEntity>): List<Long>


    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateTopics(entities: List<CountryEntity>)

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Transaction
    suspend fun upsertTopics(entities: List<CountryEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreTopics,
        updateMany = ::updateTopics
    )

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(
        value = """
            DELETE FROM country
            WHERE id in (:ids)
        """
    )
    suspend fun deleteTopics(ids: List<String>)



}