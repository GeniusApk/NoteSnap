package com.example.notesnap

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesnap.databinding.ActivityShowInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class showInfo : AppCompatActivity() {
    private val binding: ActivityShowInfoBinding by lazy {
        ActivityShowInfoBinding.inflate(layoutInflater)
    }

    private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        val noteId = intent.getStringExtra("noteId")
        val currentUser = auth.currentUser

        binding.upd.setOnClickListener {
            // If it's an edit mode, update the existing note in Firebase
            if (isEditMode) {
                if (noteId != null && currentUser != null) {
                    val newTitle = binding.title.editText?.text.toString().trim()
                    val newDescription = binding.description.editText?.text.toString().trim()
                    updateNoteInFirebase(newTitle, newDescription, noteId)
                }
            }
        }

        binding.del.setOnClickListener {
            // If it's an edit mode, delete the existing note from Firebase
            if (isEditMode) {
                if (noteId != null && currentUser != null) {
                    deleteNoteFromFirebase(currentUser.uid, noteId)
                }
            }
        }

        // Fetch the specific note from Firebase
        if (noteId != null && currentUser != null) {
            databaseReference.child("users").child(currentUser.uid).child("notes").child(noteId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val noteItem = snapshot.getValue(NoteItem::class.java)

                        // Check if the noteItem is not null
                        if (noteItem != null) {
                            // Populate the UI with note data
                            binding.title.editText?.setText(noteItem.title)
                            binding.description.editText?.setText(noteItem.description)
                            isEditMode = true // Enable edit mode after loading data
                        } else {
                            // Handle the case where noteItem is null
                            Toast.makeText(
                                this@showInfo,
                                "Failed to retrieve note data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle any errors during data retrieval
                        Toast.makeText(
                            this@showInfo,
                            "Failed to retrieve note data: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    // Function to delete an existing note from Firebase
    private fun deleteNoteFromFirebase(userId: String, noteId: String) {
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val noteRef = databaseReference.child("users").child(user.uid).child("notes")

            // Delete the specific note in Firebase using its noteId
            noteRef.child(noteId).removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note Deleted Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to Delete Note", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    // Function to update an existing note in Firebase
    private fun updateNoteInFirebase(newTitle: String, newDescription: String, noteId: String) {
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val noteRef = databaseReference.child("users").child(user.uid).child("notes")

            // Create a new NoteItem with updated values
            val updatedNote = NoteItem(newTitle, newDescription, noteId)

            // Update the specific note in Firebase using its noteId
            noteRef.child(noteId).setValue(updatedNote)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to Update Note", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}


