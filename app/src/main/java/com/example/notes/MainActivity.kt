package com.example.notes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.adapter.NotesAdapter
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.viewmodel.NoteVIewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotesAdapter
    private lateinit var noteRV:RecyclerView

    private lateinit var noteList: NoteDatabase
    private lateinit var data: List<NotesData>
    lateinit var  viewmodel:NoteVIewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewmodel=NoteVIewModel(application)
        noteRV=binding.noteRecyclerView
        binding.addNote.setOnClickListener {
            val intent=Intent(this,AddNoteActivity::class.java)
            startActivity(intent)
        }
        data= listOf()
        setupRecyclerView()
        deleteAll()
    }
    private fun setupRecyclerView() {
        viewmodel.getAll().observe(this@MainActivity){
            val adapter = NotesAdapter(
                it
            )
            // Load initial data
            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    adapter.refreshData(it)
                    binding.noteRecyclerView.adapter = adapter
                }
            }
        }

    }

    private fun deleteAll(){

        binding.deleteAll.setOnClickListener {
            lifecycleScope.launch {
                viewmodel.deleteAllNotes()

            }
            viewmodel.getAll().observe(this){it->


                    adapter = NotesAdapter(data)
                    adapter.refreshData(it)
                    binding.noteRecyclerView.adapter = adapter

            }
        }
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
        viewmodel.getAll().observe(this@MainActivity){
            val adapter = NotesAdapter(
                it
            )
            adapter.refreshData(it)
            adapter.notifyItemRemoved(it.size-1)
            updateUI(it)
        }

    }

    @Deprecated("Deprecated in Java", ReplaceWith("finish()"))
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        finish()
    }
}