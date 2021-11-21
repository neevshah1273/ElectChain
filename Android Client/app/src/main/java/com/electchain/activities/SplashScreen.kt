package com.electchain.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.electchain.R
import com.electchain.utils.Constants.userUid
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Handler().postDelayed({
            val intent = if (user == null) {
                Intent(this, MainActivity::class.java)
            } else {
                if (user.uid == userUid) {
                    Intent(this, AdminMainActivity::class.java)
                } else {
                    Intent(this, VoterMainActivity::class.java)
                }
            }
            startActivity(intent)
            finish()
        }, 3000)
    }
}