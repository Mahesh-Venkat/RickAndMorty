package com.maheshchukka.rickandmorty.ui.characters

import com.maheshchukka.rickandmorty.domain.model.CharacterModel

data class CharacterState(
    val characters: List<CharacterModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)
