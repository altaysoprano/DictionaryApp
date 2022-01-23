package com.example.dictionary.presentation.history_screen

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dictionary.common.Resource
import com.example.dictionary.data.repository.WordRepository
import com.example.dictionary.domain.model.WordInfo
import com.example.dictionary.domain.usecase.DeleteAllWords
import com.example.dictionary.domain.usecase.GetAllWordInfos
import com.example.dictionary.domain.usecase.GetWordInfoFromHistory
import com.example.dictionary.presentation.WordInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val getAllWordInfos: GetAllWordInfos,
    private val getWordInfoFromHistory: GetWordInfoFromHistory,
    private val deleteAllWords: DeleteAllWords
) : ViewModel() {

    private val _state = mutableStateOf(WordInfoState())
    val state: State<WordInfoState> = _state

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    val limit = mutableStateOf(20)
    var recipeListScrollPosition = 0


    private var searchJob: Job? = null

    init {
        getAllWords(limit.value)
    }

    fun onSearch(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500L)
            getWordInfoFromHistory(query, limit.value).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.value = state.value.copy(
                            wordInfoItems = result.data?.asReversed() ?: emptyList(),
                            isLoading = true
                        )
                    }
                    is Resource.Success -> {
                        _state.value = state.value.copy(
                            wordInfoItems = result.data?.asReversed() ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            searchedWordInfoItems = result.searchedData ?: emptyList(),
                            wordInfoItems = result.data?.asReversed() ?: emptyList(),
                            isLoading = false
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    private fun getAllWords(limit: Int) {
        getAllWordInfos(limit).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        searchedWordInfoItems = result.searchedData ?: emptyList(),
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onCloseClicked() {
        _searchQuery.value = ""
    }

    fun onClearClicked() {
        deleteAllWords().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun incrementLimit() {
        limit.value = limit.value + 20
    }

    fun onChangeRecipeScrollPosition(position: Int){
        recipeListScrollPosition = position
    }

    fun nextPage() {
        incrementLimit()
        getAllWordInfos(limit.value).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = state.value.copy(
                        isLoading = true
                    )
                }
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        searchedWordInfoItems = result.searchedData ?: emptyList(),
                        wordInfoItems = result.data ?: emptyList(),
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }


}
