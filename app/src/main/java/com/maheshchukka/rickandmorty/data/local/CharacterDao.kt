package com.maheshchukka.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacterEntity(character: CharacterEntity)

    @Query(
        "SELECT * FROM character_entity"
    )
    fun getCharacters(): List<CharacterEntity>

    @Query(
        "SELECT * FROM character_entity WHERE characterId = :characterId"
    )
    fun getCharacterDetails(characterId: Long): CharacterEntity?

    @Query("DELETE FROM character_entity")
    suspend fun clearCharacters()
}
