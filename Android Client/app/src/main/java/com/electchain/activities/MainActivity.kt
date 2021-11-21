package com.electchain.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.Fragment
import com.electchain.R
import com.electchain.fragments.CandidateFragment
import com.electchain.fragments.ElectionFragment
import com.electchain.fragments.VoterFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        findViewById<Button>(R.id.voterBtn).setOnClickListener {
            startActivity(Intent(this, VoterLoginActivity::class.java))
        }

        findViewById<Button>(R.id.adminBtn).setOnClickListener {
            startActivity(Intent(this, AdminLoginActivity::class.java))
        }

    }
}