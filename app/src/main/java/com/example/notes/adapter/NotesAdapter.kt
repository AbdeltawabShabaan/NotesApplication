package com.example.notes.adapter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.MainActivity
import com.example.notes.R
import com.example.notes.UpdateNoteActivity
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData
import com.example.notes.viewmodel.NoteVIewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale

class NotesAdapter(
    private var notesData: List<NotesData>
) : Adapter<NotesAdapter.NoteViewHolder>() {
    class NoteViewHolder(view: View) : ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTV)
        val description: TextView = view.findViewById(R.id.descriptionTV)
        val updateButton: ImageView = view.findViewById(R.id.updateButton)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)
        val time: TextView = view.findViewById(R.id.timeTV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notesData.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = notesData[position]
        holder.title.text = item.title
        holder.description.text = item.description
        val calendar = SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault()).format(item.date)

        holder.time.text = calendar

        //send data to updateNoteActivity and navigate to it
        holder.updateButton.setOnClickListener {
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", item.id)
                putExtra("note_title", item.title)
                putExtra("note_description", item.description)
            }
            holder.itemView.context.startActivity(intent)
        }

        //delete data from room and recycler view
        holder.deleteButton.setOnClickListener {
            val viewModel=NoteVIewModel(Application())
            showDeleteWarming(holder){
                viewModel.deleteNote(item)
            }
            viewModel.getAll().observe(holder.itemView.context as MainActivity){
                refreshData(it)
            }
        }
    }
    fun showDeleteWarming(holder: NoteViewHolder,onConfirm: () -> Unit){
        MaterialAlertDialogBuilder(holder.itemView.context)
            .setTitle("Delete Note")
            .setMessage("Are you sure you want to delete this note?")
            .setPositiveButton("Delete"){_,_->
                onConfirm()
            }
            .setNegativeButton("Cancel"){dialog,_->
                dialog.dismiss()
            }
            .show()

    }

    //notify data that updated in NoteData
    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNote: List<NotesData>) {
        notesData = newNote
        notifyDataSetChanged()
    }
}