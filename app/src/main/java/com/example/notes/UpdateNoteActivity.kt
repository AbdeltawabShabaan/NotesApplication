package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData
import com.example.notes.databinding.ActivityUpdateNoteBinding
import com.example.notes.viewmodel.NoteVIewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateNoteActivity : AppCompatActivity() {
    lateinit var vIewModel: NoteVIewModel
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var noteDatabase: NoteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityUpdateNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        noteDatabase=NoteDatabase.getInstance(this)
        vIewModel= NoteVIewModel(application)
        val noteId=intent.getIntExtra("note_id",-1)
        val noteTitle=intent.getStringExtra("note_title")
        val noteDescription=intent.getStringExtra("note_description")
        if (noteId==-1){
            finish()
            return
        }
        binding.updateTitleEditText.setText(noteTitle)
        binding.updateSubjectEditText.setText(noteDescription)
        binding.updateSaveButton.setOnClickListener {
            val title=binding.updateTitleEditText.text.toString()
            val description=binding.updateSubjectEditText.text.toString()
            val note=NotesData(noteId,title,description)
            if (title.isNotEmpty() && description.isNotEmpty()){
                lifecycleScope.launch(Dispatchers.IO) {
                    vIewModel.updateNote(note)
                }
                finish()
                Toast.makeText(this,"update succeed",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"update failed: empty text",Toast.LENGTH_SHORT).show()
            }

        }
    }
}