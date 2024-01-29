package com.example.notesnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.notesnap.databinding.ActivityAddnoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class addnote : AppCompatActivity() {
    private val binding: ActivityAddnoteBinding by lazy{
        ActivityAddnoteBinding.inflate(layoutInflater)
    }

    private lateinit var databaseRefence: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val noteTitle = intent.getStringExtra("NoteTitle")
        val noteDescription = intent.getStringExtra("NoteDescription")

        // Set the note information to the EditText fields
        binding.title.editText?.setText(noteTitle)
        binding.description.editText?.setText(noteDescription)









        //th

        databaseRefence= FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        binding.savebtn.setOnClickListener {
            val title = binding.title.editText?.text.toString().trim()
            val description = binding.description.editText?.text.toString().trim()

            if (title.isEmpty() && description.isEmpty()) {
                Toast.makeText(this, "Please fill both", Toast.LENGTH_SHORT).show()
            } else {

                val currentUser = auth.currentUser
                currentUser?.let { user->
                    val noteKey = databaseRefence.child("users").child(user.uid).child("notes").push().key
                    val noteItem = NoteItem(title, description )
                    if (noteKey != null) {
                       // Log.d("AddNoteActivity", "Note key: $noteKey")
                       // Log.d("AddNoteActivity", "Note item: $noteItem")

                        databaseRefence.child("users").child(user.uid).child("notes").child(noteKey)
                            .setValue(noteItem)

                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    //Log.d("AddNoteActivity", "Notes Save Successful")

                                    Toast.makeText(this, "Notes Save Successful", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                   // Log.e("AddNoteActivity", "Failed to Save", task.exception)

                                    Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
                                }
                            }
                       // Log.d("AddNoteActivity", "Database reference path: ${databaseRefence.child("users").child(currentUser?.uid ?: "null").child("notes").child(noteKey)}")
                       // Log.d("AddNoteActivity", "Current user UID: ${currentUser?.uid}")


                    }
                }
            }
        }


    }
}