package com.example.dictionary.data.repository

import android.util.Log
import com.example.dictionary.common.Resource
import com.example.dictionary.data.local.SearchedWordInfoDao
import com.example.dictionary.data.local.WordInfoDao
import com.example.dictionary.data.local.entity.SearchedWordInfoDatabase
import com.example.dictionary.data.local.entity.WordInfoEntity
import com.example.dictionary.data.remote.DictionaryApi
import com.example.dictionary.domain.model.WordInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class WordRepository(
    private val api: DictionaryApi,
    private val dao: WordInfoDao,
    private val searchedDao: SearchedWordInfoDao
) {

    fun getWordInfo(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        val wordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        val searchedWordInfos = searchedDao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Loading(searchedData = searchedWordInfos))

        try {
            val remoteWordInfos = api.getWordInfo(word)
            dao.deleteWordInfos(remoteWordInfos.map { it.word })
            dao.insertWordInfos(remoteWordInfos.map { it.toWordInfoEntity() })
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = "Oops, something went wrong!",
                    data = wordInfos,
                    searchedData = searchedWordInfos
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    message = "Couldn't reach server, check your internet connection.",
                    data = wordInfos,
                    searchedData = searchedWordInfos
                )
            )
        }

        val newWordInfos = dao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(searchedWordInfos, newWordInfos))
    }

    fun getAllWords(limit: Int): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        val limitedSearchResults = mutableListOf<WordInfo>()
        val searchResults = searchedDao.getAllWordInfos().asReversed().map { it.toWordInfo() }
        if(searchResults.size >= limit) {
            for(i in 0 until limit) {
                limitedSearchResults.add(searchResults[i])
            }
        }
        else {
            for(word in searchResults) {
                limitedSearchResults.add(word)
            }
        }
        emit(Resource.Success(data = limitedSearchResults.toList()))
    }

    fun getWordInfoFromHistory(word: String): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        val searchResults = searchedDao.getWordInfos(word).map { it.toWordInfo() }
        emit(Resource.Success(data = searchResults))
    }

    suspend fun addWordInfoToSearchedDatabase(word: WordInfo) {
        searchedDao.deleteWordInfos(word.word)
        searchedDao.insertWordInfos(listOf(word).map { it.toWordInfoEntity() })
    }

    fun deleteAllWords(): Flow<Resource<List<WordInfo>>> = flow {
        emit(Resource.Loading())
        searchedDao.deleteAllWords()
        emit(Resource.Success(data = listOf()))
    }
}
