package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.model.data.LanguageTag
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepo

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class LanguageListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val netSource: NetSource,
    private val stationsRepo: StationsRepo,
) : ViewModel() {

    val languageListState: StateFlow<LanguageUiState> = combine(
        stationsRepo.getLanguageList()
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







