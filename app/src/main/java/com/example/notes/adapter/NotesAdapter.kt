package com.example.notes.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.R
import com.example.notes.UpdateNoteActivity
import com.example.notes.database.NoteDatabase
import com.example.notes.database.model.NotesData

class NotesAdapter(
    private var notesData: List<NotesData>,
) : Adapter<NotesAdapter.NoteViewHolder>() {
    class NoteViewHolder(view: View) : ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.titleTV)
        val description: TextView = view.findViewById(R.id.descriptionTV)
        val updateButton: ImageView = view.findViewById(R.id.updateButton)
        val deleteButton: ImageView = view.findViewById(R.id.deleteButton)

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
            val noteDatabase=NoteDatabase.getInstance(holder.itemView.context).getNotDao()
            noteDatabase.deleteNote(item)
            val noteList=noteDatabase.getAll()
            refreshData(noteList)
        }
    }

    //notify data that updated in NoteData
    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newNote: List<NotesData>) {
        notesData = newNote
        notifyDataSetChanged()
    }
}