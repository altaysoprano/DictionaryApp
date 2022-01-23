package com.example.dictionary.data.local

import androidx.room.*
import com.example.dictionary.data.local.entity.WordInfoEntity
import com.example.dictionary.domain.model.WordInfo

@Dao
interface SearchedWordInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWordInfos(words : List<WordInfoEntity>)

    @Query("DELETE FROM wordinfoentity WHERE word LIKE :word || '%'")
    suspend fun deleteWordInfos(word: String)

    @Query("SELECT * FROM wordinfoentity WHERE word LIKE :word || '%'")
    suspend fun getWordInfos(word: String): List<WordInfoEntity>

    @Query("SELECT * FROM wordinfoentity")
    suspend fun getAllWordInfos(): List<WordInfoEntity>

    @Query("DELETE FROM wordinfoentity")
    suspend fun deleteAllWords()
}