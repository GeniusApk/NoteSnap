package com.example.notesnap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notesnap.databinding.ActivityLoginBinding
import com.example.notesnap.databinding.ActivitySingupBinding
import com.google.firebase.auth.FirebaseAuth

class singup : AppCompatActivity() {
    private val binding: ActivitySingupBinding by lazy{
        ActivitySingupBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        binding.singbtn.setOnClickListener {
            val email = binding.emailsingup.editText?.text.toString().trim()
            val password = binding.passwordsingup.editText?.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerWithEmailPassword(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun registerWithEmailPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration success, update UI or navigate to the next screen
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // If registration fails, display a message to the user.
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}