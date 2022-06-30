package com.google.samples.apps.nowinandroid.core.data.repository

import android.util.Log
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.Synchronizer
import com.google.samples.apps.nowinandroid.core.data.changeStationSync
import com.google.samples.apps.nowinandroid.core.data.model.asEntity
import com.google.samples.apps.nowinandroid.core.database.dao.StationDao
import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.datastore.ChangeListVersions
import com.google.samples.apps.nowinandroid.core.datastore.NiaPreferences
import com.google.samples.apps.nowinandroid.core.datastore.PreferencesStore
import com.google.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.model.data.StationsTag
import com.google.samples.apps.nowinandroid.core.network.NiANetwork
import com.google.samples.apps.nowinandroid.core.network.model.NetworkStation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*

import javax.inject.Inject
import kotlinx.coroutines.launch

class OfflineFirstStationsRepository @Inject constructor(
    private val sationDao: StationDao,
    private val network: NiANetwork,
    private val niaPreferences: NiaPreferences,
    private val preferences: PreferencesStore,
    private val localStationsSource: LocalStationsSource,
) : StationsRepository {
    override fun getAllStationsStream(): Flow<List<Station>> = sationDao.getAllStationEntitiesStream().map { it.map(StationEntity::asExternalModel) }

    override fun getTopVisitedStationsStream(): Flow<List<Station>> = flow { localStationsSource.getTopClickStationsList()}

    override fun gettopVotedStationsStream(): Flow<List<Station>> = flow { localStationsSource.gettopVotedStationsList()}

    override fun getLateUpdateStationsStream(): Flow<List<Station>> = flow { localStationsSource.getLateUpdateStationsList()}

    override fun getnowPlayingStationsStream(): Flow<List<Station>> = flow { localStationsSource.getLastClickStationsList()}

    override fun getStationsTagStream(): Flow<List<String>> = flow { localStationsSource.getStationsTagList()}

    override fun getTagList(): Flow<List<StationsTag>> = flow {
        localStationsSource.getStationsTagList()?.let { emit(it) }
    }

    override fun getCountryList(): Flow<List<String>> {
        TODO("Not yet implemented")
    }

    override fun getLanguageList(): Flow<List<LanguageTag>>  = flow {
        localStationsSource.getLanguageList()?.let { emit(it) }
    }

    override fun getStationsByConditionList(): Flow<List<Station>> = flow {
        val type = preferences.get("type","").first()
        val param = preferences.get("param","").first()
        Log.e("aaa", "type"+ type)
        Log.e("param", "param"+ param)

        localStationsSource.getStationsByConditionList(type, param)?.let { emit(it) }
    }


    override fun getFavoriteStations(): Flow<List<Station>> = sationDao.getFavoritedStations().map{ it.map(StationEntity::asExternalModel)}

    override fun setFavoriteStation(entitie: Station) {
        GlobalScope.launch(Dispatchers.IO) {
            sationDao.setFavoritedStation(entitie.asExternalModel())
        }
    }

    override fun setPlayHistory(entitie: Station) {
        GlobalScope.launch(Dispatchers.IO) {
            sationDao.setPlayHistory(entitie.asExternalModel())
        }
    }

    override fun getPlayHistory(): Flow<List<Station>> = sationDao.getPlayHistory().map { it.map(StationEntity::asExternalModel) }

    override fun getStationbyIdsEntitiesStream(entities: List<String>): Flow<List<Station>> =
        sationDao.getStationbyIdsEntitiesStream(HashSet(entities)).map{ it.map(StationEntity::asExternalModel)}

    override fun getFollowedStationIdsStream(): Flow<Set<String>> = niaPreferences.followedAuthorIds

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean =
        synchronizer.changeStationSync(
            versionReader = ChangeListVersions::stationVersion,
            changeListFetcher = { currentVersion -> network.getTopicChangeList(after = currentVersion) },
            versionUpdater = { latestVersion -> copy(topicVersion = latestVersion) },
            modelDeleter = sationDao::deleteStations,
            modelUpdater = { changedIds ->
                val netWorkStations =  localStationsSource.getLocalStationsList("topclick", "100")
                if (netWorkStations != null) { sationDao.upsertStations(entities = netWorkStations.map(NetworkStation::asEntity)) }
            }
        )
}
