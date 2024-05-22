package com.example.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData
import com.example.notes.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.subjectEditText.text.toString()
            val note = NotesData(0, title, description)
            if (title.isNotEmpty() && description.isNotEmpty()) {
                val insert = NoteDatabase.getInstance(this)
                insert.getNotDao().insertNote(note)

                finish()
                Toast.makeText(this, "add successes", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "please add text!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}