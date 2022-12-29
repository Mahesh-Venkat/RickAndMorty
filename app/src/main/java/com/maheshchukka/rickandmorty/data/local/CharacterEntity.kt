package com.maheshchukka.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_entity")
data class CharacterEntity(
    @PrimaryKey val characterId: Long,
    val name: String?,
    val status: String?,
    val species: String?,
    val gender: String?,
    val origin: String?,
    val location: String?,
    val imageUrl: String?
)
