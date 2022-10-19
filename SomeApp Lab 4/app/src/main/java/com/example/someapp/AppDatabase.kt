package com.example.someapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.someapp.Flashcard
import com.example.someapp.FlashcardDao

@Database(entities = [Flashcard::class] , version = 1)

abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
}
