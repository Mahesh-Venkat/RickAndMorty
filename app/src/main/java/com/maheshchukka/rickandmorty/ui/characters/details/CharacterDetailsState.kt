package com.maheshchukka.rickandmorty.ui.characters.details

import com.maheshchukka.rickandmorty.domain.model.CharacterModel

data class CharacterDetailsState(
    val character: CharacterModel = CharacterModel(
        location = null,
        characterId = -1,
        name = null,
        status = null,
        gender = null,
        imageUrl = null,
        origin = null,
        species = null
    ),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
