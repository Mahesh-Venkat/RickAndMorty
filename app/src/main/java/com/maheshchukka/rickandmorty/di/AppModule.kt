package com.maheshchukka.rickandmorty.di

import android.app.Application
import androidx.room.Room
import com.maheshchukka.rickandmorty.data.local.RickAndMortyDatabase
import com.maheshchukka.rickandmorty.data.remote.RickyAndMortyApi
import com.maheshchukka.rickandmorty.data.remote.RickyAndMortyApi.Companion.BASE_URL
import com.maheshchukka.rickandmorty.data.repository.CharacterRepositoryImpl
import com.maheshchukka.rickandmorty.data.repository.LocationRepositoryImpl
import com.maheshchukka.rickandmorty.domain.repository.CharacterRepository
import com.maheshchukka.rickandmorty.domain.repository.LocationRepository
import com.maheshchukka.rickandmorty.domain.usecases.GetCharacterDetailsUseCase
import com.maheshchukka.rickandmorty.domain.usecases.GetCharactersUseCase
import com.maheshchukka.rickandmorty.domain.usecases.GetLocationsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRickAndMortyApi(): RickyAndMortyApi {
        val logger = HttpLoggingInterceptor()
        logger.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRickAndMortyDatabase(app: Application): RickAndMortyDatabase {
        return Room.databaseBuilder(
            app,
            RickAndMortyDatabase::class.java,
            RickAndMortyDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(
        rickAndMortyDatabase: RickAndMortyDatabase,
        rickyAndMortyApi: RickyAndMortyApi
    ): CharacterRepository {
        return CharacterRepositoryImpl(database = rickAndMortyDatabase, api = rickyAndMortyApi)
    }

    @Provides
    @Singleton
    fun provideGetCharactersUseCase(characterRepository: CharacterRepository): GetCharactersUseCase {
        return GetCharactersUseCase(characterRepository = characterRepository)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        rickAndMortyDatabase: RickAndMortyDatabase,
        rickyAndMortyApi: RickyAndMortyApi
    ): LocationRepository {
        return LocationRepositoryImpl(database = rickAndMortyDatabase, api = rickyAndMortyApi)
    }

    @Provides
    @Singleton
    fun provideGetLocationsUseCase(locationRepository: LocationRepository): GetLocationsUseCase {
        return GetLocationsUseCase(locationRepository = locationRepository)
    }

    @Provides
    @Singleton
    fun provideGetCharacterDetailsUseCase(characterRepository: CharacterRepository): GetCharacterDetailsUseCase {
        return GetCharacterDetailsUseCase(characterRepository = characterRepository)
    }
}
