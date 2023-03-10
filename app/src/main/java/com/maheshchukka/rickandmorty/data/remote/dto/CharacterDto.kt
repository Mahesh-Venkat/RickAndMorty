package com.maheshchukka.rickandmorty.data.remote.dto

import com.squareup.moshi.Json

data class CharacterDto(
    @field:Json(name = "info") val info: Info,
    @field:Json(name = "results") val results: List<CharacterResult>
)

data class CharacterResult(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "status") val status: String?,
    @field:Json(name = "species") val species: String?,
    @field:Json(name = "type") val type: String,
    @field:Json(name = "gender") val gender: String,
    @field:Json(name = "origin") val origin: OriginOrLocation?,
    @field:Json(name = "location") val location: OriginOrLocation?,
    @field:Json(name = "image") val imageUrl: String?,
    @field:Json(name = "episode") val episodes: List<String>?
)

data class OriginOrLocation(
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "url") val url: String?
)
