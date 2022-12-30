package com.maheshchukka.rickandmorty.data.remote

import com.maheshchukka.rickandmorty.data.remote.dto.CharacterDto
import retrofit2.http.GET
import retrofit2.http.Query

interface RickyAndMortyApi {
    @GET("api/character")
    suspend fun getCharacters(@Query("page") page: Int): CharacterDto

    @GET("api/location")
    suspend fun getLocations(@Query("page") page: Int): CharacterDto

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/"
    }
}
