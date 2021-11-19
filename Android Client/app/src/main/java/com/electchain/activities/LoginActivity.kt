package com.electchain.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.electchain.R
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    val context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val etMobileNumber = findViewById<EditText>(R.id.etMobileNumber)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val tvRegisterBtn = findViewById<TextView>(R.id.tvRegisterBtn)

        tvRegisterBtn.setOnClickListener {
            startActivity(Intent(context, RegisterActivity::class.java))
        }
    }
}