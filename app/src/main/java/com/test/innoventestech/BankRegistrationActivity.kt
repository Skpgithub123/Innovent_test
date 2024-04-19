package com.test.innoventestech

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BankRegistrationActivity : ComponentActivity() {

    private lateinit var editTextPannumber: EditText
    private lateinit var editTextDate: EditText
    private lateinit var editTextMonth: EditText
    private lateinit var editTextYear: EditText
    private lateinit var buttonNext: Button
    private lateinit var txtdonthavepan: TextView

    private val TAG = "BankRegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bankreg)


        // Initialize views
        editTextPannumber = findViewById(R.id.editTextPannumber)
        editTextDate = findViewById(R.id.editTextDate)
        editTextMonth = findViewById(R.id.editTextMonth)
        editTextYear = findViewById(R.id.editTextYear)
        buttonNext = findViewById(R.id.buttonNext)
        txtdonthavepan = findViewById(R.id.txtdonthavepan)

        buttonNext.setOnClickListener(View.OnClickListener {

            checkValidation()

        })

        txtdonthavepan.setOnClickListener(View.OnClickListener {

            finish()
        })

    }

    fun checkValidation() {
        val pannumber = editTextPannumber.text.toString().trim()
        val date = editTextDate.text.toString().trim()
        val month = editTextMonth.text.toString().trim()
        val year = editTextYear.text.toString().trim()
        val dateStr = "$month/$date/$year"
        val dateFormat = "MM/dd/yyyy"
        //To validate the entered PAN number
        validateAndProcessPAN(pannumber)

        //To validate the entered birthday
        if (isValidBirthday(dateStr, dateFormat)) {
            // Valid birthday
            Log.d(TAG, "Valid birthday: $dateStr")
        } else {
            // Invalid birthday
            Log.d(TAG, "Invalid birthday: $dateStr")
        }
        if (pannumber.isEmpty() || date.isEmpty() || month.isEmpty() || year.isEmpty()) {
            Toast.makeText(
                this@BankRegistrationActivity,
                "Please enter pannumber,date,month and year",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!isValidPAN(pannumber)) {
            Toast.makeText(this@BankRegistrationActivity, "PAN is not valid", Toast.LENGTH_SHORT)
                .show()
        } else if (!isValidBirthday(dateStr, dateFormat)) {
            Toast.makeText(
                this@BankRegistrationActivity,
                "Birthday is not valid",
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            Toast.makeText(
                this@BankRegistrationActivity,
                "Details submitted successfully",
                Toast.LENGTH_SHORT
            )
                .show()
            finish()
        }
    }

    fun isValidPAN(panNumber: String): Boolean {
        val pattern = Regex("^[A-Z]{5}[0-9]{4}[A-Z]$")

        return pattern.matches(panNumber)
    }

    fun validateAndProcessPAN(enteredPAN: String) {
        if (isValidPAN(enteredPAN)) {

            Log.d(TAG, "Valid PAN: $enteredPAN")
        } else {
            Log.d(TAG, "Invalid PAN: $enteredPAN")
        }
    }

    fun isValidDateFormat(dateStr: String, dateFormat: String): Boolean {
        return try {
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            sdf.isLenient = false
            sdf.parse(dateStr) != null
        } catch (e: Exception) {
            false
        }
    }


    fun isValidBirthday(dateStr: String, dateFormat: String): Boolean {
        if (!isValidDateFormat(dateStr, dateFormat)) {
            return false
        }

        val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
        val currentDate = Calendar.getInstance().time

        return try {
            val birthDate = sdf.parse(dateStr)
            birthDate?.let {
                it <= currentDate && it > getMinAllowedDate()
            } ?: false
        } catch (e: Exception) {
            false
        }
    }

    private fun getMinAllowedDate(): Date {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, -150)
        return cal.time
    }

}