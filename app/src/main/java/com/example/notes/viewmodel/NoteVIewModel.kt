package com.example.notes.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Dao
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData

class NoteVIewModel(app: Application): ViewModel() {
    private val noteDataBase= NoteDatabase.getInstance(app)
    private val dao= noteDataBase.getNotDao()
    private val repository= NoteRepository(dao)

    //method for get all Data
    fun getAll(): LiveData<List<NotesData>> = repository.getAll()

    //method for insert note
    fun insert(notesData: NotesData)=repository.insertNote(notesData)

    //method for update note
    suspend fun updateNote(notesData: NotesData)=  repository.updateData(notesData)

    //method for delete note
    fun deleteNote(notesData: NotesData)=repository.deleteData(notesData)

    //method for delete all notes
    suspend fun deleteAllNotes()=repository.deleteAll()
}