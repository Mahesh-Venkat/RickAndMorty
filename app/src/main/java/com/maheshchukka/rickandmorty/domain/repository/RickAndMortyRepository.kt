package com.maheshchukka.rickandmorty.domain.repository

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

interface RickAndMortyRepository {
    suspend fun getCharacters(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CharacterModel>>>
}
