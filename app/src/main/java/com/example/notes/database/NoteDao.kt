package com.example.notes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.notes.database.model.NotesData
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table ORDER BY id ASC")
    fun getAll(): List<NotesData>

    @Insert
    fun insertNote(notesData: NotesData)

    @Delete
    fun deleteNote(notesData: NotesData)

    @Update
    suspend fun updateNote(notesData: NotesData)

    @Query("DELETE FROM note_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM note_table WHERE title LIKE :searchQuery")
    fun searchNote(searchQuery: String): Flow<List<NotesData>>
}