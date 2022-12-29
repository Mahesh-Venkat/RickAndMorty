package com.maheshchukka.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterPagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<CharacterPagingKeysEntity>)

    @Query("SELECT * FROM character_paging_keys WHERE characterId = :characterId")
    suspend fun pagingKeysByCharacterId(characterId: Long): CharacterPagingKeysEntity?

    @Query("DELETE FROM character_paging_keys")
    suspend fun clearCharacterPagingKeys()
}
