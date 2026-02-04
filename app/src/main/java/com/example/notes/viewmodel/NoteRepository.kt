package com.example.notes.viewmodel

import androidx.lifecycle.LiveData
import com.example.notes.database.NoteDao
import com.example.notes.database.model.NotesData

class NoteRepository(private val noteDao: NoteDao) {
    fun getAll(): LiveData<List<NotesData>> = noteDao.getAll()
    fun insertNote(noteData: NotesData)=noteDao.insertNote(noteData)
    suspend fun updateData(notesData: NotesData)=noteDao.updateNote(notesData)
    fun deleteData(notesData: NotesData)=noteDao.deleteNote(notesData)
    suspend fun deleteAll()=noteDao.deleteAll()
}