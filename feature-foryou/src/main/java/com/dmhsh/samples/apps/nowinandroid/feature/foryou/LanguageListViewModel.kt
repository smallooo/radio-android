package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.data.LocalStationsSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepository
import com.dmhsh.samples.apps.nowinandroid.core.database.model.StationEntity
import com.dmhsh.samples.apps.nowinandroid.core.database.model.asExternalModel
import com.dmhsh.samples.apps.nowinandroid.core.model.data.*

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LanguageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val localStationsSource: LocalStationsSource,
    private val stationsRepository: StationsRepository,
) : ViewModel() {

    val languageListState: StateFlow<LanguageUiState> = combine(
        stationsRepository.getLanguageList()
    ) { availableTags ->

        LanguageUiState.Tags(tags = availableTags.get(0))
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LanguageUiState.Loading)
}

sealed interface LanguageUiState {
    object Loading : LanguageUiState

    data class Tags(
        val tags: List<LanguageTag>,
    ) : LanguageUiState

    object Empty : LanguageUiState
}







