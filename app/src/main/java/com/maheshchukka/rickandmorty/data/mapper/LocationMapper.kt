package com.maheshchukka.rickandmorty.data.mapper // ktlint-disable filename

import DataItem.Header.id
import com.maheshchukka.rickandmorty.data.local.CharacterEntity
import com.maheshchukka.rickandmorty.data.local.LocationEntity
import com.maheshchukka.rickandmorty.data.remote.dto.CharacterResult
import com.maheshchukka.rickandmorty.data.remote.dto.LocationResult
import com.maheshchukka.rickandmorty.domain.model.CharacterModel
import com.maheshchukka.rickandmorty.domain.model.LocationModel

fun LocationResult.toLocationEntity(): LocationEntity {
    return LocationEntity(
        locationId = id,
        name = name,
        type = type,
        dimension = dimension,
        infoUrl = infoUrl,
        created = created
    )
}

fun LocationEntity.toLocation(): LocationModel {
    return LocationModel(
        locationId = id,
        name = name,
        type = type,
        dimension = dimension,
        infoUrl = infoUrl,
        created = created
    )
}
