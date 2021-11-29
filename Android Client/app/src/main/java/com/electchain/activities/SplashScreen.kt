package com.electchain.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.utils.Constants
import com.electchain.utils.Constants.BASE_URL
import com.electchain.utils.Constants.retrofit
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.RouterService
import com.electchain.utils.SessionManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
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
        Handler().postDelayed({
            val token = sessionManager.fetchAuthToken()
            val intent = if (token == null) {
                Intent(this, MainActivity::class.java)
            } else {
                val jwt: JWT? = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }
                val isAdmin: Boolean? = jwt?.getClaim("is_admin")?.asBoolean()
                if (isAdmin == true) {
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