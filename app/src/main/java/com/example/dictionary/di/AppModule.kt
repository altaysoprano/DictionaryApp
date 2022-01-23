package com.example.dictionary.di

import android.app.Application
import androidx.room.Room
import com.example.dictionary.data.local.Converters
import com.example.dictionary.data.local.entity.SearchedWordInfoDatabase
import com.example.dictionary.data.local.entity.WordInfoDatabase
import com.example.dictionary.data.remote.DictionaryApi
import com.example.dictionary.data.repository.WordRepository
import com.example.dictionary.data.util.GsonParser
import com.example.dictionary.domain.usecase.*
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun provideGetWordInfoUseCase(wordRepository : WordRepository) : GetWordInfo {
        return GetWordInfo(wordRepository)
    }

    @Provides
    @Singleton
    fun provideGetAllWordInfosUseCase(wordRepository: WordRepository) : GetAllWordInfos {
        return GetAllWordInfos(wordRepository)
    }

    @Provides
    @Singleton
    fun provideGetDeleteAllWords(wordRepository: WordRepository) : DeleteAllWords {
        return DeleteAllWords(wordRepository)
    }

    @Provides
    @Singleton
    fun provideGetWordInfoFromHistory(wordRepository: WordRepository) : GetWordInfoFromHistory {
        return GetWordInfoFromHistory(wordRepository)
    }

    @Provides
    @Singleton
    fun provideAddWordToSearchedDb(wordRepository : WordRepository) : AddWordToSearchedDb {
        return AddWordToSearchedDb(wordRepository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        db : WordInfoDatabase,
        api : DictionaryApi,
        searchedDb: SearchedWordInfoDatabase
    ) : WordRepository {
        return WordRepository(api, db.dao, searchedDb.searchedDao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabase(app : Application) : WordInfoDatabase {
        return Room.databaseBuilder(
            app, WordInfoDatabase::class.java, "word_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideSearchedWordInfoDatabase(app : Application) : SearchedWordInfoDatabase {
        return Room.databaseBuilder(
            app, SearchedWordInfoDatabase::class.java, "searched_word_db"
        ).addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun provideDictionaryApi() : DictionaryApi{
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }
}