package com.example.dictionary.domain.model

import com.example.dictionary.data.local.entity.WordInfoEntity
import com.example.dictionary.data.remote.dto.MeaningDto
import com.example.dictionary.data.remote.dto.PhoneticDto

data class WordInfo(
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val word: String
) {
    fun toWordInfoEntity() : WordInfoEntity {
        return WordInfoEntity(
            meanings = meanings,
            origin = origin,
            phonetic = phonetic,
            word = word
        )
    }
}