package com.maheshchukka.rickandmorty.data.repository

import com.maheshchukka.rickandmorty.data.local.RickAndMortyDatabase
import com.maheshchukka.rickandmorty.data.mapper.toLocation
import com.maheshchukka.rickandmorty.data.mapper.toLocationEntity
import com.maheshchukka.rickandmorty.data.remote.RickyAndMortyApi
import com.maheshchukka.rickandmorty.data.remote.dto.LocationResult
import com.maheshchukka.rickandmorty.domain.model.LocationModel
import com.maheshchukka.rickandmorty.domain.repository.LocationRepository
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class LocationRepositoryImpl(
    private val api: RickyAndMortyApi,
    private val database: RickAndMortyDatabase
) : LocationRepository {
    private val dao = database.locationDao

    /**
     *  1. if fetchRemote flag is false then we will get the db results, if the db is empty then
     *  we will go fetch it from the API.
     *  2. Fetching from the API, it runs in a loop to get all the Locations until the next page is null
     *  and then add them to the database,
     *  fetch them from the database in descending sorted order by name
     */
    override suspend fun getLocations(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<LocationModel>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val localGetLocations = dao.getLocations()

            emit(
                Resource.Success(
                    data = localGetLocations.map { locationEntity ->
                        locationEntity.toLocation()
                    }.sortedBy { location -> location.name }
                )
            )
            val isDbEmpty = localGetLocations.isEmpty()
            val justLoadLocationsFromCache = !isDbEmpty && !fetchFromRemote

            if (justLoadLocationsFromCache) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteLocations = try {
                var pageNumber = 1
                val locationResults = mutableListOf<LocationResult>()
                do {
                    val currentLocations =
                        api.getLocations(page = pageNumber++)
                    locationResults.addAll(currentLocations.results)
                } while (currentLocations.info.next != null)

                locationResults.sortedBy { locationResult -> locationResult.name }
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data", data = null))
                emit(Resource.Loading(isLoading = false))
                null
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data", data = null))
                emit(Resource.Loading(isLoading = false))
                null
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Couldn't load data", data = null))
                emit(Resource.Loading(isLoading = false))
                null
            }

            remoteLocations?.let { locations ->
                if (locations.isNotEmpty()) {
                    dao.clearLocations()

                    dao.insertAll(

                        locations.map { locationResult -> locationResult.toLocationEntity() }
                    )

                    emit(
                        Resource.Success(
                            data = dao
                                .getLocations()
                                .map { locationEntity -> locationEntity.toLocation() }
                                .sortedBy { location -> location.name }
                        )
                    )
                    emit(Resource.Loading(isLoading = false))
                } else {
                    emit(
                        Resource.Error(
                            message = "Couldn't find the Locations",
                            data = null
                        )
                    )
                    emit(Resource.Loading(isLoading = false))
                }
            }
        }
    }
}
