package com.maheshchukka.rickandmorty.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maheshchukka.rickandmorty.domain.usecases.GetCharactersUseCase
import com.maheshchukka.rickandmorty.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Characters Fragment"
    }
    val text: LiveData<String> = _text

    private val _state = MutableStateFlow<CharacterState>(CharacterState())
    val state: StateFlow<CharacterState> = _state

    init {
        getCharacters()
    }

    fun onEvent(event: CharacterEvent) {
        when (event) {
            is CharacterEvent.Refresh -> {
                _state.value = state.value.copy(
                    isRefreshing = true
                )
                getCharacters(fetchFromRemote = true)
            }
        }
    }

    private fun getCharacters(
        fetchFromRemote: Boolean = false
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getCharactersUseCase(fetchFromRemote = fetchFromRemote)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { characters ->
                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(
                                        characters = characters,
                                        error = null,
                                        isRefreshing = false
                                    )
                                }
                            }
                        }
                        is Resource.Error -> {
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(
                                    error = result.message
                                )
                            }
                        }
                        is Resource.Loading -> {
                            withContext(Dispatchers.Main) {
                                _state.value = state.value.copy(
                                    isLoading = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }
}
