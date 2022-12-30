package com.maheshchukka.rickandmorty.ui.characters.details

import androidx.lifecycle.*
import com.maheshchukka.rickandmorty.domain.usecases.GetCharacterDetailsUseCase
import com.maheshchukka.rickandmorty.ui.characters.CharacterEvent
import com.maheshchukka.rickandmorty.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CharacterDetailsViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Characters Fragment"
    }
    val text: LiveData<String> = _text

    val characterId = savedStateHandle.get<Long>("characterId") ?: Long.MIN_VALUE

    private val _state = MutableStateFlow<CharacterDetailsState>(CharacterDetailsState())
    val state: StateFlow<CharacterDetailsState> = _state

    init {
        getCharacterDetails(characterId = characterId)
    }

    fun onEvent(event: CharacterEvent) {
        when (event) {
            is CharacterEvent.Refresh -> {
                _state.value = state.value.copy(
                    isRefreshing = true
                )
                getCharacterDetails(fetchFromRemote = true, characterId = characterId)
            }
        }
    }

    private fun getCharacterDetails(
        fetchFromRemote: Boolean = false,
        characterId: Long
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            getCharacterDetailsUseCase(fetchFromRemote = fetchFromRemote, characterId = characterId)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { characterModel ->
                                withContext(Dispatchers.Main) {
                                    _state.value = state.value.copy(
                                        character = characterModel,
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
                                    isLoading = result.isLoading,
                                    isRefreshing = result.isLoading
                                )
                            }
                        }
                    }
                }
        }
    }
}
