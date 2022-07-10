package com.dmhsh.samples.apps.nowinandroid.feature.foryou


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.data.repository.StationsRepo
import com.dmhsh.samples.apps.nowinandroid.core.model.data.StationsTag

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class TagListViewModel @Inject constructor(
    stationsRepo: StationsRepo, ) : ViewModel() {

    var tagListState: StateFlow<TagUiState> =
        stationsRepo.getTagList().map{ availableTags ->
        TagUiState.Tags(tags = availableTags)
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





