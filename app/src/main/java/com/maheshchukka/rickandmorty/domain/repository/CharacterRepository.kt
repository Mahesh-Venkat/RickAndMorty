package com.maheshchukka.rickandmorty.domain.repository

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CharacterModel>>>

    suspend fun getCharacterDetails(
        fetchFromRemote: Boolean,
        characterId: Long
    ): Flow<Resource<CharacterModel>>
}
