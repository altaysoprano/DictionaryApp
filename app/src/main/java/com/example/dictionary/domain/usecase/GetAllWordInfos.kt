package com.example.dictionary.domain.usecase

import com.example.dictionary.common.Resource
import com.example.dictionary.data.repository.WordRepository
import com.example.dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow

class GetAllWordInfos(
    private val repository: WordRepository
) {
    operator fun invoke(limit: Int) : Flow<Resource<List<WordInfo>>> {
        return repository.getAllWords(limit)
    }
}