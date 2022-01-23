package com.example.dictionary.presentation

import com.example.dictionary.domain.model.WordInfo

data class WordInfoState(
    val searchedWordInfoItems: List<WordInfo> = emptyList(),
    val wordInfoItems: List<WordInfo> = emptyList(),
    val isLoading: Boolean = false
)
