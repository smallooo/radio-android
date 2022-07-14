/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource
import com.dmhsh.samples.apps.nowinandroid.core.model.data.Station
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.QUERY_KEY
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.SEARCH_BACKENDS_KEY
import com.dmhsh.samples.apps.nowinandroid.playback.PlaybackConnection
import com.hdmsh.core_ui_playback.searching.errors.ApiCaptchaError
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tm.alashow.base.util.extensions.getStateFlow
import tm.alashow.base.util.extensions.stateInDefault

const val SEARCH_DEBOUNCE_MILLIS = 400L

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    handle: SavedStateHandle,
    //private val snackbarManager: SnackbarManager,
    private val remoteSource: NetSource,
    private val playbackConnection: PlaybackConnection,
) : ViewModel() {
    private val initialQuery = handle.get(QUERY_KEY) ?: ""
    private val searchQueryState = handle.getStateFlow(initialQuery, viewModelScope, initialQuery)
    private val searchFilterState = handle.getStateFlow("search_filter", viewModelScope, SearchFilter.from(handle.get(SEARCH_BACKENDS_KEY)))
    private val searchTriggerState = handle.getStateFlow("search_trigger", viewModelScope, SearchTrigger(initialQuery))
    private val captchaError = MutableStateFlow<ApiCaptchaError?>(null)
    private val pendingActions = MutableSharedFlow<SearchAction>()
    private val onSearchEventChannel = Channel<SearchEvent>(Channel.CONFLATED)
    val onSearchEvent = onSearchEventChannel.receiveAsFlow()



    val state = combine(searchFilterState, captchaError, ::SearchViewState)
        .stateInDefault(viewModelScope, SearchViewState.Empty)

    class LocalStationsContract {

        data class State(
            var localStations: List<Station> = listOf(),
            var isLoading: Boolean = false,
            var isWaiting: Boolean = true
        )


    }

    var stateS by mutableStateOf(
        LocalStationsContract.State(
            localStations = listOf(),
            isLoading = false,
            isWaiting = true
        )
    )



    val searchQuery = searchQueryState.asStateFlow()


    init {
        viewModelScope.launch {
            pendingActions.collectLatest { action ->
                when (action) {
                    is SearchAction.QueryChange -> {
                        searchQueryState.value = action.query
                        Log.e("aaa searchQuery", action.query)
                        // trigger search while typing if minerva is the only backend selected
                        if (searchFilterState.value.hasMinervaOnly) {
                            searchTriggerState.value = SearchTrigger(searchQueryState.value)
                        }
                    }
                    is SearchAction.Search -> {
                        searchTriggerState.value = SearchTrigger(searchQueryState.value)
                    }
                    is SearchAction.SelectBackendType -> selectBackendType(action)
                    is SearchAction.SubmitCaptcha -> {
                        Log.e("aaa submitCaptcha", searchQueryState.value)
                        submitCaptcha(action)
                    }
                    //is SearchAction.SubmitCaptcha -> submitCaptcha(action)
                   // is SearchAction.AddError -> snackbarManager.addError(action.error)
                  //  is SearchAction.ClearError -> snackbarManager.removeCurrentError()
                   // is SearchAction.PlayRadio -> playbackConnection.playAudio(station)
                }
            }
        }

        viewModelScope.launch {
            combine(searchTriggerState, searchFilterState, ::SearchEvent)
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .collectLatest {
                   // changeStatus(it)
                    search(it)
                    onSearchEventChannel.send(it)
                }
        }
    }

    fun changeStatus(searchEvent: SearchEvent) {
        val (trigger, filter) = searchEvent
        val query = trigger.query
        val searchParams = DatmusicSearchParams(query, trigger.captchaSolution)
        val backends = filter.backends.joinToString { it.type }

        Log.e("aaa SearchEvent", searchQueryState.value)

        if(query.isNotBlank()) {

            val aaa = "it"
            viewModelScope.launch {
            stateS = aaa.let { stateS.copy(isWaiting = false, isLoading = true) }
                }

        }
    }


    fun search(searchEvent: SearchEvent) {
        val (trigger, filter) = searchEvent
        val query = trigger.query
        val searchParams = DatmusicSearchParams(query, trigger.captchaSolution)
        val backends = filter.backends.joinToString { it.type }

        Log.e("aaa SearchEvent", searchQueryState.value)

        if(query.isNotBlank()) {

            val aaa = "it"
            stateS = aaa.let { stateS.copy(isWaiting = false, isLoading = true) }

            viewModelScope.launch {
                val categories = remoteSource.getSearch("byname", query)
                if (categories != null) {
                    stateS = categories.let { stateS.copy(localStations = it, isLoading = false) }
//                    stateS.isWaiting = false
//                    stateS.isLoading = true
                   // effects.send(LocalStationsContract.Effect.DataWasLoaded)
                }
            }
        }
    }

    /**
     * Resets captcha error and triggers search with given captcha solution.
     */
    private fun submitCaptcha(action: SearchAction.SubmitCaptcha) {
        captchaError.value = null
        searchTriggerState.value = SearchTrigger(
            query = searchQueryState.value,
            captchaSolution = CaptchaSolution(
                action.captchaError.error.captchaId,
                action.captchaError.error.captchaIndex,
                action.solution
            )
        )
    }

    fun submitAction(action: SearchAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    /**
     * Sets search filter to only given backend if [action.selected] otherwise resets to [SearchFilter.DefaultBackends].
     */
    private fun selectBackendType(action: SearchAction.SelectBackendType) {
        //analytics.event("search.selectBackend", mapOf("type" to action.backendType))
        searchFilterState.value = searchFilterState.value.copy(
            backends = when (action.selected) {
                true -> setOf(action.backendType)
                else -> SearchFilter.DefaultBackends
            }
        )
    }

}




