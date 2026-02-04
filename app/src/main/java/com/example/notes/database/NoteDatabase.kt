package com.example.notes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.notes.database.model.NotesData

@Database(entities = [NotesData::class], version = 2)
abstract class NoteDatabase :RoomDatabase() {
    abstract fun getNotDao():NoteDao
    companion object{
        private const val DATABASE_NAME="note database"
        private var noteDatabaseInstance:NoteDatabase?=null
        fun getInstance(context: Context):NoteDatabase{
            if (noteDatabaseInstance==null){
                noteDatabaseInstance= Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                )   .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return noteDatabaseInstance!!
        }
    }
}