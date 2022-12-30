package com.maheshchukka.rickandmorty.ui.locations

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.model.LocationModel

data class LocationState(
    val locations: List<LocationModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
