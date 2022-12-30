package com.maheshchukka.rickandmorty.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location_entity")
data class LocationEntity(
    @PrimaryKey val locationId: Long,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val infoUrl: String?,
    val created: String?,
)
