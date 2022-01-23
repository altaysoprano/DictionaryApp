package com.example.dictionary.data.local.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dictionary.data.local.Converters
import com.example.dictionary.data.local.SearchedWordInfoDao
import com.example.dictionary.data.local.WordInfoDao

@Database(
    entities = [WordInfoEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class SearchedWordInfoDatabase : RoomDatabase() {
    abstract val searchedDao : SearchedWordInfoDao
}