package com.maheshchukka.rickandmorty.domain.repository

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.model.LocationModel
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocations(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<LocationModel>>>
}
