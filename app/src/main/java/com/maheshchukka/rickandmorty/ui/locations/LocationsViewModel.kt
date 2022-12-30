package com.maheshchukka.rickandmorty.ui.locations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maheshchukka.rickandmorty.domain.usecases.GetLocationsUseCase
import com.maheshchukka.rickandmorty.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LocationState>(LocationState())
    val state: StateFlow<LocationState> = _state

    init {
        getLocations()
    }

    fun onEvent(event: LocationEvent) {
        when (event) {
            is LocationEvent.Refresh -> {
                getLocations(fetchFromRemote = true)
            }
        }
    }

    private fun getLocations(
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getLocationsUseCase(fetchFromRemote = fetchFromRemote)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { locations ->
                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(
                                        locations = locations,
                                        error = null
                                    )
                                }
                            }
                        }
                        is Resource.Error -> {
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(
                                    error = result.message
                                )
                            }
                        }
                        is Resource.Loading -> {
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }
}
