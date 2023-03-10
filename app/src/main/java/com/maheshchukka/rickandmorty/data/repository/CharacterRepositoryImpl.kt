package com.maheshchukka.rickandmorty.data.repository

import com.maheshchukka.rickandmorty.data.local.RickAndMortyDatabase
import com.maheshchukka.rickandmorty.data.mapper.toCharacter
import com.maheshchukka.rickandmorty.data.mapper.toCharacterEntity
import com.maheshchukka.rickandmorty.data.remote.RickyAndMortyApi
import com.maheshchukka.rickandmorty.data.remote.dto.CharacterResult
import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.repository.CharacterRepository
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class CharacterRepositoryImpl(
    private val api: RickyAndMortyApi,
    private val database: RickAndMortyDatabase
) : CharacterRepository {
    private val dao = database.characterDao

    /**
     *  1. if fetchRemote flag is false then we will get the db results, if the db is empty then
     *  we will go fetch it from the API.
     *  2. Fetching from the API, it runs in a loop to get all the characters until the next page is null
     *  and then add them to the database,
     *  fetch them from the database in descending sorted order by name
     */
    override suspend fun getCharacters(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CharacterModel>>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val localGetCharacters = dao.getCharacters()

            emit(
                Resource.Success(
                    data = localGetCharacters.map { characterEntity ->
                        characterEntity.toCharacter()
                    }.sortedBy { character -> character.name }
                )
            )
            val isDbEmpty = localGetCharacters.isEmpty()
            val justLoadCharactersFromCache = !isDbEmpty && !fetchFromRemote

            if (justLoadCharactersFromCache) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteCharacters = try {
                var pageNumber = 1
                val characterResults = mutableListOf<CharacterResult>()
                do {
                    val currentCharacters =
                        api.getCharacters(page = pageNumber++)
                    characterResults.addAll(currentCharacters.results)
                } while (currentCharacters.info.next != null)

                characterResults
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

            remoteCharacters?.let { remoteCharacters ->
                if (remoteCharacters.isNotEmpty()) {
                    dao.clearCharacters()

                    dao.insertAll(

                        remoteCharacters.map { characterResult -> characterResult.toCharacterEntity() }
                    )

                    emit(
                        Resource.Success(
                            data = dao
                                .getCharacters()
                                .map { characterEntity -> characterEntity.toCharacter() }
                                .sortedBy { character -> character.name }
                        )
                    )
                    emit(Resource.Loading(isLoading = false))
                } else {
                    emit(
                        Resource.Error(
                            message = "Couldn't find the Characters",
                            data = null
                        )
                    )
                    emit(Resource.Loading(isLoading = false))
                }
            }
        }
    }

    override suspend fun getCharacterDetails(
        fetchFromRemote: Boolean,
        characterId: Long
    ): Flow<Resource<CharacterModel>> {
        return flow {
            emit(Resource.Loading(isLoading = true))

            val localGetCharacterDetails = dao.getCharacterDetails(characterId)

            emit(
                Resource.Success(
                    data = localGetCharacterDetails?.toCharacter()
                )
            )
            val isDbEmpty = localGetCharacterDetails == null
            val justLoadCharacterDetailsFromCache = !isDbEmpty && !fetchFromRemote

            if (justLoadCharacterDetailsFromCache) {
                emit(Resource.Loading(isLoading = false))
                return@flow
            }

            val remoteCharacterDetails = try {
                api.getCharacters(characterId)
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

            remoteCharacterDetails?.let { remoteCharacter ->
                dao.clearCharacters()

                dao.insertCharacterEntity(
                    remoteCharacter.toCharacterEntity()
                )

                emit(
                    Resource.Success(
                        data = dao
                            .getCharacterDetails(characterId = characterId)?.toCharacter()
                    )
                )
                emit(Resource.Loading(isLoading = false))
            }
        }
    }
}
