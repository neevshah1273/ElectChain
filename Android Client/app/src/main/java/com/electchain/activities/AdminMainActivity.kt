package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.electchain.R
import com.electchain.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val registrationFragment = RegistrationFragment()
        val addCandidateFragment = AddCandidateFragment()
        val phaseFragment = PhaseFragment()
        val adminFragment = AdminFragment()

        makeCurrentFragment(registrationFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_registration -> makeCurrentFragment(registrationFragment)
                R.id.ic_add_candidate -> makeCurrentFragment(addCandidateFragment)
                R.id.ic_phase -> makeCurrentFragment(phaseFragment)
                R.id.ic_admin-> makeCurrentFragment(adminFragment)
            }
            true
        }
    }
    private fun makeCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flWrapper, fragment)
            commit()
        }
    }
}