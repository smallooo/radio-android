package com.google.samples.apps.nowinandroid.feature.foryou


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.google.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.google.samples.apps.nowinandroid.core.database.model.StationEntity
import com.google.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.google.samples.apps.nowinandroid.core.model.data.FollowableStation
import com.google.samples.apps.nowinandroid.core.model.data.FollowableTag
import com.google.samples.apps.nowinandroid.core.model.data.Station
import com.google.samples.apps.nowinandroid.core.model.data.StationsTag

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TagListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localStationsSource: LocalStationsSource,
    private val stationsRepository: StationsRepository,
) : ViewModel() {

    val tagListState: StateFlow<TagUiState> = combine(
        stationsRepository.getTagList()
    ) { availableTags ->
        Log.e("aaa", "availableTags")
        TagUiState.Tags(tags = availableTags.get(0))
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TagUiState.Loading)
}

sealed interface TagUiState {
    object Loading : TagUiState

    data class Tags(
        val tags: List<StationsTag>,
    ) : TagUiState

    object Empty : TagUiState
}





