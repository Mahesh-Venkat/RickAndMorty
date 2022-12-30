package com.maheshchukka.rickandmorty.data.remote.dto

import com.squareup.moshi.Json

data class LocationDto(
    @field:Json(name = "info") val info: Info,
    @field:Json(name = "results") val results: List<LocationResult>
)


data class LocationResult(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "type") val type: String?,
    @field:Json(name = "dimension") val dimension: String?,
    @field:Json(name = "residents") val residents: List<String>,
    @field:Json(name = "url") val infoUrl: String,
    @field:Json(name = "created") val created: String?,
)