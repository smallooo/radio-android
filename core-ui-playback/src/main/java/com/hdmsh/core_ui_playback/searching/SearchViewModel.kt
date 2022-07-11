/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.hdmsh.core_ui_playback.searching

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.QUERY_KEY
import com.dmhsh.samples.apps.nowinandroid.core.navigation.Screens.SEARCH_BACKENDS_KEY
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
internal class SearchViewModel @Inject constructor(
    handle: SavedStateHandle,

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

    val searchQuery = searchQueryState.asStateFlow()

    init {
        viewModelScope.launch {
            pendingActions.collectLatest { action ->
                when (action) {
                    is SearchAction.QueryChange -> {
                        searchQueryState.value = action.query

                        // trigger search while typing if minerva is the only backend selected
                        if (searchFilterState.value.hasMinervaOnly) {
                            searchTriggerState.value = SearchTrigger(searchQueryState.value)
                        }
                    }
                    is SearchAction.Search -> searchTriggerState.value = SearchTrigger(searchQueryState.value)
                   // is SearchAction.SelectBackendType -> selectBackendType(action)
                    is SearchAction.SubmitCaptcha -> submitCaptcha(action)
                }
            }
        }

        viewModelScope.launch {
            combine(searchTriggerState, searchFilterState, ::SearchEvent)
                .debounce(SEARCH_DEBOUNCE_MILLIS)
                .collectLatest {
                    search(it)
                    onSearchEventChannel.send(it)
                }
        }
    }

    fun search(searchEvent: SearchEvent) {
        val (trigger, filter) = searchEvent
        val query = trigger.query
        val searchParams = DatmusicSearchParams(query, trigger.captchaSolution)
        val backends = filter.backends.joinToString { it.type }
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
}
