package com.electchain.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.electchain.R

class VoterLoginActivity : AppCompatActivity() {
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val etMobileNumber = findViewById<EditText>(R.id.etMobileNumber).text.toString().trim()
        val etVoterId = findViewById<EditText>(R.id.etVoterId).text.toString().trim()
        findViewById<Button>(R.id.btnNext).setOnClickListener {
            if (checkValidation(etMobileNumber, etVoterId)) {
                Log.d("TAG", "Field are entered.")
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkValidation(etMobileNumber: String, etVoterId: String): Boolean {
        return etMobileNumber.isNotEmpty() && etVoterId.isNotEmpty()
    }
}