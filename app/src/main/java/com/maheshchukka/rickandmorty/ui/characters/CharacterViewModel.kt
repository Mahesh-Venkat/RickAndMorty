package com.maheshchukka.rickandmorty.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharacterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Characters Fragment"
    }
    val text: LiveData<String> = _text
}
