package com.example.notesnap

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.notesnap.databinding.NotesItemRecBinding

class NoteAdapter(private val notes: List<NoteItem>):RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    class NoteViewHolder(private val binding : NotesItemRecBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteItem) {

            binding.titleNotesItem.text= note.title
            binding.descriptionNotesItem.text = note.description
            binding.cardview.setOnClickListener {

                val intent = Intent(binding.root.context, showInfo::class.java)
                intent.putExtra("NoteTitle", note.title)
                intent.putExtra("NoteDescription", note.description)
                intent.putExtra("noteId", note.noteId)
                binding.root.context.startActivity(intent)

                // Pass any data to the new activity if needed
                // M
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NotesItemRecBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return NoteViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)

    }
}