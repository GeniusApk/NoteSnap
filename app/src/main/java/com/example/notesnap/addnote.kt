package com.example.notesnap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notesnap.databinding.ActivityAddnoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class addnote : AppCompatActivity() {
    private val binding: ActivityAddnoteBinding by lazy {
        ActivityAddnoteBinding.inflate(layoutInflater)
    }
    // private lateinit var recyclerView: RecyclerView


    private lateinit var databaseRefence: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




        databaseRefence = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()


        binding.savebtn.setOnClickListener {
            val title = binding.title.editText?.text.toString().trim()
            val description = binding.description.editText?.text.toString().trim()

            if (title.isEmpty() && description.isEmpty()) {
                Toast.makeText(this, "Please fill both", Toast.LENGTH_SHORT).show()
            } else {

                val currentUser = auth.currentUser
                currentUser?.let { user ->
                    val noteKey =
                        databaseRefence.child("users").child(user.uid).child("notes").push().key
                    val noteItem = NoteItem(title, description, noteKey ?: "")
                    if (noteKey != null) {
                        databaseRefence.child("users").child(user.uid).child("notes")
                            .child(noteKey)
                            .setValue(noteItem)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        this,
                                        "Notes Save Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                }
            }
        }
    }
}

