package com.google.samples.apps.nowinandroid.feature.foryou

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountryViewModel @Inject constructor(private val remoteSource: FoodMenuRemoteSource) :
    ViewModel() {

    var state by mutableStateOf(
        FoodCategoriesContract.State(
            categories = listOf(),
            isLoading = true
        )
    )
        private set

    var effects = Channel<FoodCategoriesContract.Effect>(UNLIMITED)
        private set

    init {
        viewModelScope.launch { getCountriesList() }
    }

    private suspend fun getCountriesList() {
        val categories = remoteSource.getCountryList()
        viewModelScope.launch {
            state = categories?.let { state.copy(categories = it, isLoading = false) }!!
            effects.send(FoodCategoriesContract.Effect.DataWasLoaded)
        }


    }

}



