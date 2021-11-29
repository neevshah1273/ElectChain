package com.electchain.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import com.electchain.R
import com.electchain.models.Result
import com.electchain.models.User
import com.electchain.utils.Constants
import com.electchain.utils.Constants.BASE_URL
import com.electchain.utils.Constants.retrofit
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.RouterService
import com.electchain.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VoterLoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_login)

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

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            var etMobileNumber = findViewById<EditText>(R.id.etMobileNumber).text.toString().trim()
            val etVoterId = findViewById<EditText>(R.id.etVoterId).text.toString().trim()
            if (checkValidation(etMobileNumber, etVoterId)) {
                if (etMobileNumber.length == 10) {
                    etMobileNumber = "91$etMobileNumber"
                    val user = User()
                    user.phone = etMobileNumber
                    voterLogin(user)
                } else {
                    Toast.makeText(this, "Mobile number must be of 10 digits.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun voterLogin(user: User) {
        val call: Call<Result> = routerService.voterLogin(user)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result: Result = response.body()!!
                    Toast.makeText(applicationContext, result.message, Toast.LENGTH_SHORT).show()
                    val intent = Intent(applicationContext, VerifyActivity::class.java)
                    intent.putExtra("phone", user.phone)
                    startActivity(intent)
                } else if (response.code() == 400) {
                    Toast.makeText(applicationContext, "Something went wrong. Cannot send otp.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(applicationContext, "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkValidation(etMobileNumber: String, etVoterId: String): Boolean {
        return etMobileNumber.isNotEmpty()
    }
}