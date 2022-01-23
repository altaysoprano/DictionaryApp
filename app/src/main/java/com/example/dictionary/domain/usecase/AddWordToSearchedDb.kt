package com.example.dictionary.domain.usecase

import com.example.dictionary.data.local.entity.WordInfoEntity
import com.example.dictionary.data.repository.WordRepository
import com.example.dictionary.domain.model.WordInfo

class AddWordToSearchedDb(
    private val repository: WordRepository
) {
    suspend operator fun invoke(word : WordInfo){
        return repository.addWordInfoToSearchedDatabase(word)
    }
}