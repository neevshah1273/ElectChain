package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
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

        val electionFragment = ElectionFragment()
        val candidateFragment = CandidateFragment()
        val voterFragment = VoterFragment()

        makeCurrentFragment(electionFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_election -> makeCurrentFragment(electionFragment)
                R.id.ic_candidate -> makeCurrentFragment(candidateFragment)
                R.id.ic_account -> makeCurrentFragment(voterFragment)
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