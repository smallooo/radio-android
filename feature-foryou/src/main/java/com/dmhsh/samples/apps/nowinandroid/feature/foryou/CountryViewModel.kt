package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.CountrySource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val remoteSource: CountrySource) :
    ViewModel() {

    var state by mutableStateOf(
        com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.State(
            categories = listOf(),
            isLoading = true
        )
    )

    var effects = Channel<com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            getCountriesList()
        }
    }

    private suspend fun getCountriesList() {
        val categories = remoteSource.getCountryList()
        viewModelScope.launch {
            state = categories?.let { state.copy(categories = it, isLoading = false) }!!
            effects.send(com.dmhsh.samples.apps.nowinandroid.feature.foryou.CountryCategoriesContract.Effect.DataWasLoaded)
        }
    }
}



