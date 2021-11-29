package com.electchain.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.electchain.R
import com.electchain.models.Credentials
import com.electchain.models.Result
import com.electchain.utils.Constants
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.RouterService
import com.electchain.utils.SessionManager
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class AdminLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Constants.retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = Constants.retrofit.create(RouterService::class.java)
        Constants.sessionManager = SessionManager(applicationContext)

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etLoginEmail).text.toString()
            val password = findViewById<EditText>(R.id.etLoginPassword).text.toString()
            if (checkValidation(email, password)) {
                val credentials = Credentials()
                credentials.email = email
                credentials.password = password
                adminLogin(credentials)
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun adminLogin(credentials: Credentials) {
        val call: Call<Result> = routerService.adminLogin(credentials)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                when {
                    response.code() == 200 -> {
                        val result: Result = response.body()!!
                        sessionManager.saveAuthToken(result.token)
                        startActivity(Intent(applicationContext, AdminMainActivity::class.java))
                    }
                    response.code() == 400 -> {
                        Toast.makeText(applicationContext, "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 404 -> {
                        Toast.makeText(applicationContext, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(applicationContext, "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkValidation(email: String, password: String): Boolean {
        return email.isNotEmpty() && password.isNotEmpty()
    }
}