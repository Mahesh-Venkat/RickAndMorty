package com.maheshchukka.rickandmorty.data.mapper // ktlint-disable filename

import com.maheshchukka.rickandmorty.data.local.CharacterEntity
import com.maheshchukka.rickandmorty.data.remote.dto.CharacterResult
import com.maheshchukka.rickandmorty.domain.model.CharacterModel

fun CharacterResult.toCharacterEntity(): CharacterEntity {
    return CharacterEntity(
        characterId = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        origin = origin?.name,
        imageUrl = imageUrl,
        location = location?.name
    )
}

fun CharacterEntity.toCharacter(): CharacterModel {
    return CharacterModel(
        characterId = characterId,
        name = name,
        status = status,
        species = species,
        gender = gender,
        origin = origin,
        imageUrl = imageUrl,
        location = location
    )
}
