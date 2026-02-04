package com.example.notes.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("note_table")
data class NotesData(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    @ColumnInfo
    val title:String?=null,
    @ColumnInfo
    val description:String?=null,
    @ColumnInfo
    val date:Long=System.currentTimeMillis()
)
