package com.dmhsh.samples.apps.nowinandroid.feature.foryou

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dmhsh.samples.apps.nowinandroid.core.data.NetSource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val netSource: NetSource) :
    ViewModel() {

    var state by mutableStateOf(
        CountryCategoriesContract.State(
            categories = listOf(),
            isLoading = true
        )
    )

    var effects = Channel<CountryCategoriesContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch {
            getCountriesList()
        }
    }

    private suspend fun getCountriesList() {
        val categories = netSource.getCountryList()
        if(!categories.isNullOrEmpty()) {
            viewModelScope.launch {
                state = categories?.let { state.copy(categories = it, isLoading = false) }!!
                effects.send(CountryCategoriesContract.Effect.DataWasLoaded)
            }
        }
    }
}



