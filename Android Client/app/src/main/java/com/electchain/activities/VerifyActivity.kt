package com.electchain.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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


class VerifyActivity : AppCompatActivity() {
    private lateinit var otpCode: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

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
        otpInputHandler()
    }

    private fun otpInputHandler() {
        val etOtpBox1 = findViewById<EditText>(R.id.etOtpBox1)
        val etOtpBox2 = findViewById<EditText>(R.id.etOtpBox2)
        val etOtpBox3 = findViewById<EditText>(R.id.etOtpBox3)
        val etOtpBox4 = findViewById<EditText>(R.id.etOtpBox4)
        val etOtpBox5 = findViewById<EditText>(R.id.etOtpBox5)
        val etOtpBox6 = findViewById<EditText>(R.id.etOtpBox6)

        val edit = arrayOf<EditText>(etOtpBox1, etOtpBox2, etOtpBox3, etOtpBox4, etOtpBox5, etOtpBox6)

        etOtpBox1.addTextChangedListener(GenericTextWatcher(etOtpBox1, edit))
        etOtpBox2.addTextChangedListener(GenericTextWatcher(etOtpBox2, edit))
        etOtpBox3.addTextChangedListener(GenericTextWatcher(etOtpBox3, edit))
        etOtpBox4.addTextChangedListener(GenericTextWatcher(etOtpBox4, edit))
        etOtpBox5.addTextChangedListener(GenericTextWatcher(etOtpBox5, edit))
        etOtpBox6.addTextChangedListener(GenericTextWatcher(etOtpBox6, edit))

        findViewById<Button>(R.id.btnVerify).setOnClickListener {
            otpCode = etOtpBox1.text.toString() + etOtpBox2.text.toString() + etOtpBox3.text.toString() + etOtpBox4.text.toString() + etOtpBox5.text.toString() + etOtpBox6.text.toString()
            if (otpCode.length == 6) {
                val phone = intent.getStringExtra("phone")
                val user = User()
                user.phone = phone!!
                user.otpCode = otpCode
                verifyOtp(user)
            } else {
                Toast.makeText(applicationContext, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyOtp(user: User) {
        val call: Call<Result> = routerService.verifyOtp(user)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                when {
                    response.code() == 200 -> {
                        val result: Result = response.body()!!
                        sessionManager.saveAuthToken(result.token)
                        startActivity(Intent(applicationContext, VoterMainActivity::class.java))
                    }
                    response.code() == 400 -> {
                        Toast.makeText(applicationContext, "Something went wrong. Cannot verify entered otp.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(applicationContext, "Invalid otp entered.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(applicationContext, "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

class GenericTextWatcher(private val view: View, private val editText: Array<EditText>):
    TextWatcher {
    override fun afterTextChanged(editable: Editable) {
        val text = editable.toString()
        when (view.id) {
            R.id.etOtpBox1 -> if (text.length == 1) editText[1].requestFocus()
            R.id.etOtpBox2 -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
            R.id.etOtpBox3 -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
            R.id.etOtpBox4 -> if (text.length == 1) editText[4].requestFocus() else if (text.isEmpty()) editText[2].requestFocus()
            R.id.etOtpBox5 -> if (text.length == 1) editText[5].requestFocus() else if (text.isEmpty()) editText[3].requestFocus()
            R.id.etOtpBox6 -> if (text.isEmpty()) editText[4].requestFocus()
        }
    }
    override fun beforeTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
    override fun onTextChanged(arg0: CharSequence, arg1: Int, arg2: Int, arg3: Int) {}
}