package com.example.bdsdmsapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class FindDonor : AppCompatActivity() {

    private lateinit var spinnerSearchBloodGroup: Spinner
    private lateinit var btnSearchDonor: Button
    private lateinit var listViewDonors: ListView
    private lateinit var dbRef: DatabaseReference
    private lateinit var donorList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_donor)

        spinnerSearchBloodGroup = findViewById(R.id.spinnerSearchBloodGroup)
        btnSearchDonor = findViewById(R.id.btnSearchDonor)
        listViewDonors = findViewById(R.id.listViewDonors)
        donorList = ArrayList()

        // Spinner values
        val bloodGroups = arrayOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        spinnerSearchBloodGroup.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups)

        dbRef = FirebaseDatabase.getInstance().getReference("donors")

        btnSearchDonor.setOnClickListener {
            searchDonors()
        }
    }

    private fun searchDonors() {
        val selectedBlood = spinnerSearchBloodGroup.selectedItem.toString()

        if (selectedBlood == "Select Blood Group") {
            Toast.makeText(this, "Please select a blood group!", Toast.LENGTH_SHORT).show()
            return
        }

        dbRef.orderByChild("bloodGroup").equalTo(selectedBlood)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    donorList.clear()

                    for (donorSnap in snapshot.children) {
                        val donor = donorSnap.getValue(Donor::class.java)
                        donor?.let {
                            if (canDonate(it.lastDonation)) {
                                // یہاں زیادہ details add کر رہے ہیں
                                val donorDetails = """
                                Name: ${it.donorName}
                                Phone: ${it.phone}
                                City: ${it.city}
                                Address: ${it.address}
                                Last Donation: ${it.lastDonation ?: "Never"}
                                Donation Count: ${it.donationCount ?: "0"}
                            """.trimIndent()

                                donorList.add(donorDetails)
                            }
                        }
                    }

                    if (donorList.isEmpty()) {
                        donorList.add("No eligible donor found ❌")
                    }

                    val adapter = ArrayAdapter(this@FindDonor, android.R.layout.simple_list_item_1, donorList)
                    listViewDonors.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@FindDonor, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Function to check if 3 months have passed since last donation
    private fun canDonate(lastDonation: String?): Boolean {
        if (lastDonation.isNullOrEmpty()) return true  // Never donated -> Eligible

        return try {
            val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val lastDate = sdf.parse(lastDonation)
            val calendar = Calendar.getInstance()
            calendar.time = lastDate!!
            calendar.add(Calendar.MONTH, 3)  // Add 3 months

            val today = Calendar.getInstance().time
            today.after(calendar.time) // true if 3 months passed
        } catch (e: Exception) {
            true // If parsing fails, consider eligible
        }
    }
}
