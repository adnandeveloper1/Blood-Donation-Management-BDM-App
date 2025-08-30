package com.example.bdsdmsapp

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class Login : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        database = FirebaseDatabase.getInstance().getReference("Users")

        val etUsername = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnlogin)
        val textSignup = findViewById<TextView>(R.id.btnSigntext)

        // Navigate to Signup screen
        textSignup.setOnClickListener {
            startActivity(Intent(this, Signin::class.java))
        }

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter username & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check user in Realtime DB
            database.child(username)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val storedPassword = snapshot.child("password").getValue(String::class.java)

                            if (storedPassword == password) {
                                Toast.makeText(this@Login, "Login successful ✅", Toast.LENGTH_SHORT).show()

                                val intent = Intent(this@Login, Home::class.java)  // 👈 Home Activity open hogi
                                intent.putExtra("username", username)
                                intent.putExtra("email", snapshot.child("email").getValue(String::class.java))
                                intent.putExtra("fullname", snapshot.child("fullname").getValue(String::class.java))
                                startActivity(intent)
                                finish()
                            }

                        else {
                                Toast.makeText(this@Login, "Incorrect password ❌", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this@Login, "User '$username' not found ❌", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@Login, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}
