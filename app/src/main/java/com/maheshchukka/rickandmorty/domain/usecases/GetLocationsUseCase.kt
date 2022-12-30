package com.maheshchukka.rickandmorty.domain.usecases

import com.maheshchukka.rickandmorty.domain.model.LocationModel
import com.maheshchukka.rickandmorty.domain.repository.LocationRepository
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

class GetLocationsUseCase(
    private val locationRepository: LocationRepository
) {
    suspend operator fun invoke(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<LocationModel>>> {
        return locationRepository.getLocations(
            fetchFromRemote = fetchFromRemote
        )
    }
}
