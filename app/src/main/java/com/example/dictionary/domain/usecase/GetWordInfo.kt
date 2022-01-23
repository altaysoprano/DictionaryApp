package com.example.dictionary.domain.usecase

import com.example.dictionary.common.Resource
import com.example.dictionary.data.repository.WordRepository
import com.example.dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWordInfo(
    private val repository : WordRepository
) {
    operator fun invoke(word: String) : Flow<Resource<List<WordInfo>>> {
        if(word.isBlank()) {
            return flow {}
        }
        return repository.getWordInfo(word)
    }
}