package com.example.randomwoman.ui.theme.woman

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.randomwoman.data.remote.dto.PersonaDto
import com.example.randomwoman.data.repository.PersonRepository
import com.example.randomwoman.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PersonListState(
    val isLoading: Boolean = false,
    val persons: List<PersonaDto> = emptyList(),
    val error: String = "",
)
@HiltViewModel
class WomanViewModel @Inject constructor(
    private val personRepository: PersonRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonListState())
    val uiState: StateFlow<PersonListState> = _uiState.asStateFlow()

    init {
        personRepository.getPerson().onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }

                is Resource.Success -> {
                    _uiState.update { it.copy(persons = result.data ?: emptyList()) }
                }

                is Resource.Error -> {
                    _uiState.update { it.copy(error = result.message ?: "Error desconocido") }
                }
            }
        }.launchIn(viewModelScope)
    }

}