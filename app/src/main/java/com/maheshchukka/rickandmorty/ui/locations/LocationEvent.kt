package com.maheshchukka.rickandmorty.ui.locations

sealed class LocationEvent {
    object Refresh : LocationEvent()
}
