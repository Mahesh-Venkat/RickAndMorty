package com.maheshchukka.rickandmorty.domain.usecases

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.repository.RickAndMortyRepository
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val rickAndMortyRepository: RickAndMortyRepository
) {
    suspend operator fun invoke(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<CharacterModel>>> {
        return rickAndMortyRepository.getCharacters(
            fetchFromRemote = fetchFromRemote
        )
    }
}
