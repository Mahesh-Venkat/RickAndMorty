package com.maheshchukka.rickandmorty.domain.usecases

import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.repository.CharacterRepository
import com.maheshchukka.rickandmorty.util.Resource
import kotlinx.coroutines.flow.Flow

class GetCharacterDetailsUseCase(
    private val characterRepository: CharacterRepository
) {
    suspend operator fun invoke(
        fetchFromRemote: Boolean,
        characterId: Long
    ): Flow<Resource<CharacterModel>> {
        return characterRepository.getCharacterDetails(
            fetchFromRemote = fetchFromRemote,
            characterId = characterId
        )
    }
}
