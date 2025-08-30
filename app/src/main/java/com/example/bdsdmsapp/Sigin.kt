package com.example.bdsdmsapp

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class Signin : AppCompatActivity() {

    private lateinit var database: DatabaseReference

    companion object {
        const val KEY_EMAIL = "email"
        const val KEY_USERNAME = "username"
        const val KEY_DOB = "dob"
        const val KEY_PASSWORD = "password"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sigin)  //

        // Initialize Firebase
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Views
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etUsername = findViewById<EditText>(R.id.etUsername)
        val etDob = findViewById<EditText>(R.id.etDob)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val checkboxSignup = findViewById<CheckBox>(R.id.Checkboxsignup)
        val btnSignup = findViewById<Button>(R.id.btnSignup)
        val textLogin = findViewById<TextView>(R.id.textlogin)

        // Navigate to Login
        textLogin.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        // SignUp button
        btnSignup.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val username = etUsername.text.toString().trim()
            val dob = etDob.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || username.isEmpty() || dob.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userKey = normalizeKey(username)
            val user = mapOf(
                KEY_EMAIL to email,
                KEY_USERNAME to username,
                KEY_DOB to dob,
                KEY_PASSWORD to password
            )

            // ✅ Fixed here
            database.child(userKey).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Login::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Registration failed: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Date of Birth Picker
        etDob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                    etDob.setText(date)
                },
                year,
                month,
                day
            ).show()
        }
    }

    private fun normalizeKey(value: String): String {
        return value.lowercase().replace(".", "_")
    }
}
