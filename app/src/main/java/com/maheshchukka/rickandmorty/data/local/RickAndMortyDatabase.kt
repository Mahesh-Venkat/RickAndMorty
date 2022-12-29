package com.maheshchukka.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        CharacterEntity::class
    ],
    version = 1
)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract val characterDao: CharacterDao

    companion object {
        const val DATABASE_NAME = "ricky_and_morty_db"
    }
}
