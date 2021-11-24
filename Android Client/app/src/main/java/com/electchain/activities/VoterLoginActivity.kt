package com.electchain.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.*
import com.electchain.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class VoterLoginActivity : AppCompatActivity() {
    val context: Context = this

    private lateinit var mAuth: FirebaseAuth
    private lateinit var forceResendingToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var mCallBacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mVerificationId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_login)

        mAuth = FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnNext).setOnClickListener {
            var etMobileNumber = findViewById<EditText>(R.id.etMobileNumber).text.toString().trim()
            val etVoterId = findViewById<EditText>(R.id.etVoterId).text.toString().trim()
            if (checkValidation(etMobileNumber, etVoterId)) {
                if (etMobileNumber.length == 10) {
                    etMobileNumber = "+91$etMobileNumber"
                    mCallBacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            Log.d("TAG", "Credentials: $credential")
                            signInWithPhoneAuthCredential(credential)
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }

                        override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                        ) {
                            mVerificationId = verificationId
                            forceResendingToken = token
                            Log.d("TAG", mVerificationId!!)
                            val intent = Intent(context, VerifyActivity::class.java)
                            intent.putExtra("mVerificationId", mVerificationId)
                            startActivity(intent)
                        }
                    }
                    mAuth.setLanguageCode("en")
                    val options = PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(etMobileNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallBacks)
                        .build()
                    PhoneAuthProvider.verifyPhoneNumber(options)
                } else {
                    Toast.makeText(this, "Mobile number must be of 10 digits.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Sign In Successful")
                    val user = task.result?.user
                    Log.d("TAG", "User: $user")
                } else {
                    Log.d("TAG", "Failure: ", task.exception)
                }
            }
    }

    private fun checkValidation(etMobileNumber: String, etVoterId: String): Boolean {
        return etMobileNumber.isNotEmpty()
    }
}