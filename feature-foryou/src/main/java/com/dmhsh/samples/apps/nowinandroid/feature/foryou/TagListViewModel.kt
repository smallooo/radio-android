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
    savedStateHandle: SavedStateHandle,
    private val netSource: NetSource,
    private val stationsRepo: StationsRepo,
) : ViewModel() {

    val tagListState: StateFlow<TagUiState> = combine(
        stationsRepo.getTagList()
    ) { availableTags ->
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





