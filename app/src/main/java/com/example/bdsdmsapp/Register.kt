package com.example.bdsdmsapp
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class Register : AppCompatActivity() {

    // UI Components
    private lateinit var etDonorName: EditText
    private lateinit var etFatherName: EditText
    private lateinit var etCNIC: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var etDOB: EditText
    private lateinit var spinnerBloodGroup: Spinner
    private lateinit var etCity: EditText
    private lateinit var etAddress: EditText
    private lateinit var etPhone: EditText
    private lateinit var etEmail: EditText
    private lateinit var etLastDonation: EditText
    private lateinit var etDonationCount: EditText
    private lateinit var etWeight: EditText
    private lateinit var etMedical: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_donor)

        // Bind views
        etDonorName = findViewById(R.id.etDonorName)
        etFatherName = findViewById(R.id.etFatherName)
        etCNIC = findViewById(R.id.etCNIC)
        rgGender = findViewById(R.id.rgGender)
        etDOB = findViewById(R.id.etDOB)
        spinnerBloodGroup = findViewById(R.id.spinnerBloodGroup)
        etCity = findViewById(R.id.etCity)
        etAddress = findViewById(R.id.etAddress)
        etPhone = findViewById(R.id.etPhone)
        etEmail = findViewById(R.id.etEmail)
        etLastDonation = findViewById(R.id.etLastDonation)
        etDonationCount = findViewById(R.id.etDonationCount)
        etWeight = findViewById(R.id.etWeight)
        etMedical = findViewById(R.id.etMedical)
        btnSave = findViewById(R.id.btnSaveDonor)

        // Spinner blood groups
        val bloodGroups = arrayOf("Select Blood Group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-")
        spinnerBloodGroup.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, bloodGroups)

        // Date Pickers
        etDOB.setOnClickListener { showDatePicker(etDOB) }
        etLastDonation.setOnClickListener { showDatePicker(etLastDonation) }

        // Save button
        btnSave.setOnClickListener {
            saveDonorData()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            editText.setText("$d/${m + 1}/$y")
        }, year, month, day)
        dpd.show()
    }

    private fun saveDonorData() {
        val name = etDonorName.text.toString()
        val father = etFatherName.text.toString()
        val cnic = etCNIC.text.toString()
        val genderId = rgGender.checkedRadioButtonId
        val gender = if (genderId != -1) findViewById<RadioButton>(genderId).text.toString() else ""
        val dob = etDOB.text.toString()
        val bloodGroup = spinnerBloodGroup.selectedItem.toString()
        val city = etCity.text.toString()
        val address = etAddress.text.toString()
        val phone = etPhone.text.toString()
        val email = etEmail.text.toString()
        val lastDonation = etLastDonation.text.toString()
        val donationCount = etDonationCount.text.toString()
        val weight = etWeight.text.toString()
        val medical = etMedical.text.toString()

        if (name.isEmpty() || bloodGroup == "Select Blood Group" || phone.isEmpty()) {
            Toast.makeText(this, "Please fill required fields!", Toast.LENGTH_SHORT).show()
            return
        }

        val donorId = FirebaseDatabase.getInstance().reference.push().key!!

        val donor = Donor(
            donorId, name, father, cnic, gender, dob, bloodGroup, city, address,
            phone, email, lastDonation, donationCount, weight, medical
        )

        val dbRef = FirebaseDatabase.getInstance().getReference("donors")
        dbRef.child(donorId).setValue(donor).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "Donor Registered ✅", Toast.LENGTH_SHORT).show()
                clearFields()
            } else {
                Toast.makeText(this, "Error: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearFields() {
        etDonorName.text.clear()
        etFatherName.text.clear()
        etCNIC.text.clear()
        rgGender.clearCheck()
        etDOB.text.clear()
        spinnerBloodGroup.setSelection(0)
        etCity.text.clear()
        etAddress.text.clear()
        etPhone.text.clear()
        etEmail.text.clear()
        etLastDonation.text.clear()
        etDonationCount.text.clear()
        etWeight.text.clear()
        etMedical.text.clear()
    }
}

