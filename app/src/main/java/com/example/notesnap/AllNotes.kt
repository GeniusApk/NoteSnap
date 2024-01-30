package com.example.notesnap

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notesnap.databinding.ActivityAllNotesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AllNotes : AppCompatActivity() {
    private val binding: ActivityAllNotesBinding by lazy {
        ActivityAllNotesBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var originalNoteList: MutableList<NoteItem>
    private lateinit var filteredNoteList: MutableList<NoteItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.floatindbtn.setOnClickListener {
            val intent = Intent(this, addnote::class.java)
            startActivity(intent)
        }

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        recyclerView = binding.recyclerViewNotes

        originalNoteList = mutableListOf()
        filteredNoteList = mutableListOf()

        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val noteReference = databaseReference.child("users").child(user.uid).child("notes")
            noteReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    originalNoteList.clear()
                    for (noteSnapshot in snapshot.children) {
                        val note = noteSnapshot.getValue(NoteItem::class.java)
                        note?.let {
                            originalNoteList.add(it)
                        }
                    }
                    originalNoteList.reverse()
                    filteredNoteList.clear()
                    filteredNoteList.addAll(originalNoteList)

                    val adapter = NoteAdapter(filteredNoteList)
                    recyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled if needed
                }
            })
        }

        // Set up the SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterNotes(newText)
                }
                return true
            }
        })
    }

    private fun filterNotes(query: String) {
        filteredNoteList.clear()
        for (note in originalNoteList) {
            if (note.title.contains(query, ignoreCase = true) || note.description.contains(query, ignoreCase = true)) {
                filteredNoteList.add(note)
            }
        }

        val adapter = NoteAdapter(filteredNoteList)
        recyclerView.adapter = adapter
    }
}
