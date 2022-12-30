package com.maheshchukka.rickandmorty.data.remote

import com.maheshchukka.rickandmorty.data.remote.dto.CharacterDto
import com.maheshchukka.rickandmorty.data.remote.dto.CharacterResult
import com.maheshchukka.rickandmorty.data.remote.dto.LocationDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickyAndMortyApi {
    @GET("api/character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterDto

    @GET("api/character/{characterId}")
    suspend fun getCharacters(
        @Path("characterId") characterId: Long,
    ): CharacterResult

    @GET("api/location")
    suspend fun getLocations(@Query("page") page: Int): LocationDto

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/"
    }
}
