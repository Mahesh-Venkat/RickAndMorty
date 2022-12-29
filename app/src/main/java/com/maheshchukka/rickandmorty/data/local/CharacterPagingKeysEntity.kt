package com.maheshchukka.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "character_paging_keys")
data class CharacterPagingKeysEntity(
    @PrimaryKey val characterId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)
