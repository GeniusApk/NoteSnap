package com.example.notesnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.notesnap.databinding.ActivityAddnoteBinding
import com.example.notesnap.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addnotesbtn.setOnClickListener {
            val intent = Intent(this , addnote::class.java)
            startActivity(intent)
        }

        binding.allnotesbtn.setOnClickListener {
            val intent = Intent(this , AllNotes::class.java)
            startActivity(intent)
        }


    }
}