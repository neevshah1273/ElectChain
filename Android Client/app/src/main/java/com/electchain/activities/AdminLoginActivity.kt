package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.electchain.R

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val adminLoginEmailAddress = findViewById<EditText>(R.id.adminLoginEmailAddress).text.toString().trim()
        val adminLoginPassword = findViewById<EditText>(R.id.adminLoginPassword).text.toString().trim()

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            if (checkValidation(adminLoginEmailAddress, adminLoginPassword)) {
                Log.d("TAG", "Fields are entered")
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkValidation(adminLoginEmailAddress: String, adminLoginPassword: String): Boolean {
        return adminLoginEmailAddress.isNotEmpty() && adminLoginPassword.isNotEmpty()
    }
}