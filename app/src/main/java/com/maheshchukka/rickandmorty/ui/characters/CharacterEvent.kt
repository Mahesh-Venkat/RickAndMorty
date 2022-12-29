package com.maheshchukka.rickandmorty.ui.characters

sealed class CharacterEvent {
    object Refresh : CharacterEvent()
}
