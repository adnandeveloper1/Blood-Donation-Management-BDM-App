package com.example.bdsdmsapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bdsdmsapp.databinding.ActivityHomeBinding
import com.example.bdsdmsapp.Register

class Home : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get data from login
        val username = intent.getStringExtra("username")
        val email = intent.getStringExtra("email")
        val fullname = intent.getStringExtra("fullname")

        Toast.makeText(this, "Welcome $fullname 👋", Toast.LENGTH_SHORT).show()

        // Find Donor

        binding.btnfindDonor.setOnClickListener {
            val intent = Intent(this, FindDonor::class.java)
            startActivity(intent)
        }

        // Register Donor
        binding.btnregdonor.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }


        // Request Blood
        binding.btnRequest.setOnClickListener {
            Toast.makeText(this, "Request Blood Clicked ✅", Toast.LENGTH_SHORT).show()
        }

        // Hospital
        binding.btnHospital.setOnClickListener {
            Toast.makeText(this, "Hospital Clicked ✅", Toast.LENGTH_SHORT).show()
        }
        // UMW Website Button
        binding.btnumw.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://umw.edu.pk/blood-donation-society/") // اپنی UMW website link یہاں ڈالیں
            startActivity(intent)
        }
        binding.btnumw.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://www.facebook.com/profile.php?id=61573756687707") // اپنی UMW website link یہاں ڈالیں
            startActivity(intent)

        }
        // Team
        binding.btnteam.setOnClickListener {
            val intent = Intent(this, Team::class.java)
            startActivity(intent)
        }

        // FAQ
        binding.btnFaq.setOnClickListener {
            Toast.makeText(this, "FAQ Clicked ✅", Toast.LENGTH_SHORT).show()
        }

        // Social
        binding.btnSocial.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data =
                Uri.parse("https://www.instagram.com/blooddonationsociety_umw/") // اپنی UMW website link یہاں ڈالیں
            startActivity(intent)
        }
    }

}