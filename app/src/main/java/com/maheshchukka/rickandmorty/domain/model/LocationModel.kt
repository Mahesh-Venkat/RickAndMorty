package com.maheshchukka.rickandmorty.domain.model

import androidx.room.PrimaryKey

data class LocationModel(
    val locationId: Long,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val infoUrl: String?,
    val created: String?,
)

