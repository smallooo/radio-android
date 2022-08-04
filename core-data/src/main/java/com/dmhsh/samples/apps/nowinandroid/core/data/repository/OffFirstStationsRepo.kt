package com.dmhsh.samples.apps.nowinandroid.core.data.repository

import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.data.Synchronizer
import com.dmhsh.samples.apps.nowinandroid.core.data.changeStationSync
import com.dmhsh.samples.apps.nowinandroid.core.data.model.asEntity
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.local.LocalStationSource
import com.dmhsh.samples.apps.nowinandroid.core.database.dao.StationDao
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.dmhsh.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.dmhsh.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.dmhsh.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag
import com.dmhsh.samples.apps.nowinandroid.core.network.Dispatcher
import com.dmhsh.samples.apps.nowinandroid.core.network.NiANetwork
import com.dmhsh.samples.apps.nowinandroid.core.network.model.NetworkStation
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*

import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class OffFirstStationsRepo @Inject constructor(
    private val sationDao: StationDao,
    private val network: NiANetwork,
    private val niaPreferences: NiaPreferences,
    private val preferences: PreferencesStore,
    private val netSource: NetSource,
    private val networkJson: Json,
) : StationsRepo {
    override fun getAllStream(): Flow<List<Station>> =
        sationDao.getAllStationEntitiesStream().map {
            it.map(StationEntity::asExternalModel)
    }

    override fun getTopVisitedStream(): Flow<List<Station>> = flow {
        netSource.getTopClickList()
    }

    override val gettopVotedStream : Flow<ArrayList<Station>> =  flow {
        netSource.gettopVotedList()?.let { emit(it)}
    }

    override fun getLateUpdateStream(): Flow<List<Station>> = flow {
        netSource.getLateUpdateStationsList()
    }

    override fun getnowPlayingStream(): Flow<List<Station>> = flow {
        netSource.getLastClickList()
    }

    override fun getTagList(): Flow<List<StationsTag>> = flow {
        netSource.getTagList()?.let { emit(it) }
    }

    override fun getLanguageList(): Flow<List<LanguageTag>>  = flow {
        netSource.getLanguageList()?.let { emit(it) }
    }

    override fun getSearchByTypeList(): Flow<List<Station>> = flow {
        val type = preferences.get("type","").first()
        val param = preferences.get("param","").first()
        netSource.searchByTypeList(type, param)?.let { emit(it) }
    }

    override fun getFavorite(): Flow<List<Station>> = sationDao.getFavoritedStations().map{ it.map(StationEntity::asExternalModel)}

    @OptIn(DelicateCoroutinesApi::class)
    override fun setFavorite(entitie: Station) {
        GlobalScope.launch(Dispatchers.IO) { sationDao.setFavoritedStation(entitie.asExternalModel()) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun setPlayHistory(entitie: Station) {
        GlobalScope.launch(Dispatchers.IO) { sationDao.setPlayHistory(entitie.asExternalModel()) }
    }

    override fun getPlayHistory(): Flow<List<Station>> =
        sationDao.getPlayHistory().map {
            it.map(StationEntity::asExternalModel)
        }

    override fun getbyIdsEntitiesStream(entities: List<String>): Flow<List<Station>> =
        sationDao.getStationbyIdsEntitiesStream(HashSet(entities)).map{
            it.map(StationEntity::asExternalModel)
        }

    override fun getFollowedIdsStream(): Flow<Set<String>> =
        niaPreferences.followedAuthorIds

    @OptIn(DelicateCoroutinesApi::class)
    override fun insertOrIgnoreStation(entitie: Station) {
        GlobalScope.launch(Dispatchers.IO) {
            sationDao.insertOrIgnoreStation(entitie.asExternalModel())
        }
    }

    override fun getLocalStations(): Flow<List<Station>> = flow<List<Station>> {
        emit(networkJson.decodeFromString<List<Station>>(LocalStationSource.topVoted).map { it })
    }.flowOn(Dispatchers.IO)

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeStationSync(
            versionReader = ChangeListVersions::stationVersion,
            changeListFetcher = { currentVersion -> network.getTopicChangeList(after = currentVersion) },
            versionUpdater = { latestVersion -> copy(topicVersion = latestVersion) },
            modelDeleter = sationDao::deleteStations,
            modelUpdater = { changedIds ->
                val netWorkStations =  netSource.getLocalList("topclick", "100")
                if (netWorkStations != null) { sationDao.upsertStations(entities = netWorkStations.map(NetworkStation::asEntity)) }
            }
        )
}
