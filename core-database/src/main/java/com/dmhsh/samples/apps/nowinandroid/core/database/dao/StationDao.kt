package com.dmhsh.samples.apps.nowinandroid.core.database.dao

import androidx.room.*
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.database.model.TopicEntity
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import kotlinx.coroutines.flow.Flow


@Dao
interface StationDao {
    @Query(value = " SELECT * FROM stations WHERE stationuuid = :stationId")
    fun getStationEntityByUUID(stationId: String): Flow<StationEntity>

    @Query(value = "SELECT * FROM stations")
    fun getAllStationEntitiesStream(): Flow<List<StationEntity>>

    @Query(value = "SELECT * FROM stations WHERE favorited = 1")
    fun getFavoritedStations(): Flow<List<StationEntity>>

    @Update
    fun setFavoritedStation(entitie: StationEntity)

    @Update()
    fun setPlayHistory(entitie: StationEntity)

    @Query(value = "SELECT * FROM stations WHERE length(lastPlayedTime) > 1 ORDER BY cast(lastPlayedTime as unsigned) desc")
    fun getPlayHistory(): Flow<List<StationEntity>>

    @Query(value = "SELECT * FROM stations Limit 5")
    fun getTopVisitedStationsStream(): Flow<List<StationEntity>>

    @Query(value = " SELECT * FROM stations WHERE stationuuid IN (:ids)")
    fun getStationbyIdsEntitiesStream(ids: Set<String>): Flow<List<StationEntity>>

    @Query(value = "SELECT * FROM stations WHERE stationuuid IN (:ids)")
    fun getStationbyCountryEntitiesStream(ids: Set<String>): Flow<List<StationEntity>>

    @Query(value = "SELECT * FROM stations WHERE stationuuid IN (:ids)")
    fun getStationbyLanguagesEntitiesStream(ids: Set<String>): Flow<List<StationEntity>>

    @Query(value = " SELECT * FROM stations WHERE stationuuid IN (:ids)")
    fun getStationbyVoteEntitiesStream(ids: Set<String>): Flow<List<StationEntity>>

    @Query(value = " SELECT * FROM stations WHERE stationuuid IN (:ids)")
    fun getStationbyViewsEntitiesbyIdsStream(ids: Set<String>): Flow<List<StationEntity>>

    /**
     * Inserts [stationEntities] into the db if they don't exist, and ignores those that do
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnoreStations(stationEntities: List<StationEntity>): List<Long>

    /**
     * Updates [entities] in the db that match the primary key, and no-ops if they don't
     */
    @Update
    suspend fun updateStations(entities: List<StationEntity>)

    /**
     * Inserts or updates [entities] in the db under the specified primary keys
     */
    @Transaction
    suspend fun upsertStations(entities: List<StationEntity>) = upsert(
        items = entities,
        insertMany = ::insertOrIgnoreStations,
        updateMany = ::insertOrIgnoreStations
    )

    /**
     * Deletes rows in the db matching the specified [ids]
     */
    @Query(value = "DELETE FROM stations WHERE stationuuid in (:ids)")
    suspend fun deleteStations(ids: List<String>)
}


