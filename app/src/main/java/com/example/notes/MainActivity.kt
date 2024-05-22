package com.example.notes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.adapter.NotesAdapter
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData
import com.example.notes.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var noteRV:RecyclerView
    private lateinit var noteList: NoteDatabase
    private lateinit var data: List<NotesData>
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        noteRV=binding.noteRecyclerView
        binding.addNote.setOnClickListener {
            val intent=Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }
        data= listOf()
        setupRecyclerView(this)
        deleteAll(this)
    }
    private fun setupRecyclerView(context: Context) {
        val noteList = NoteDatabase.getInstance(context).getNotDao()

        val adapter = NotesAdapter(
            data
        )

        binding.noteRecyclerView.adapter = adapter

        // Load initial data
        lifecycleScope.launch(Dispatchers.IO) {
            val initialNotes = noteList.getAll()
            withContext(Dispatchers.Main) {
                adapter.refreshData(initialNotes)
            }
        }
    }

    private fun deleteAll(context: Context){
        noteList=NoteDatabase.getInstance(context)
        binding.deleteAll.setOnClickListener {
            lifecycleScope.launch {
                noteList.getNotDao().deleteAll()
                val updateNote=noteList.getNotDao().getAll()
                withContext(Dispatchers.Main){
                    adapter.refreshData(updateNote)
                }
            }
        }
        val initialNotes = noteList.getNotDao().getAll()
        adapter = NotesAdapter(initialNotes)
        binding.noteRecyclerView.adapter = adapter
    }
    private fun updateUI(note:List<NotesData>?){
        if (note!=null){
            if (note.isNotEmpty()){
                binding.imageEmptyNote.visibility=View.GONE
                binding.noteRecyclerView.visibility=View.VISIBLE
            }else{
                binding.imageEmptyNote.visibility=View.VISIBLE
                binding.noteRecyclerView.visibility=View.GONE
            }
        }
    }
    override fun onResume() {
        super.onResume()
        adapter.refreshData(noteList.getNotDao().getAll())
        adapter.notifyItemRemoved(data.size-1)
        updateUI(noteList.getNotDao().getAll())
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}