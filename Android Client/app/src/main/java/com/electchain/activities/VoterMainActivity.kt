package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.electchain.R
import com.electchain.fragments.CandidateFragment
import com.electchain.fragments.ElectionFragment
import com.electchain.fragments.ResultFragment
import com.electchain.fragments.VoterFragment
import com.electchain.utils.Constants
import com.electchain.utils.Constants.BASE_URL
import com.electchain.utils.Constants.retrofit
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.RouterService
import com.electchain.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VoterMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_main)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(applicationContext)

        val electionFragment = ElectionFragment()
        val candidateFragment = CandidateFragment()
        val resultFragment = ResultFragment()
        val voterFragment = VoterFragment()

        makeCurrentFragment(electionFragment)

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_election -> makeCurrentFragment(electionFragment)
                R.id.ic_candidate -> makeCurrentFragment(candidateFragment)
                R.id.ic_result -> makeCurrentFragment(resultFragment)
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