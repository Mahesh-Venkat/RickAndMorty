package com.maheshchukka.rickandmorty.domain.model

data class CharacterModel(
    val characterId: Long,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val origin: String?,
    val location: String?,
    val imageUrl: String?
)
