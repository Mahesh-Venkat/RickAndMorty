package com.maheshchukka.rickandmorty.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(locations: List<LocationEntity>)

    @Query(
        "SELECT * FROM location_entity"
    )
    fun getLocations(): List<LocationEntity>

    @Query("DELETE FROM location_entity")
    suspend fun clearLocations()
}
