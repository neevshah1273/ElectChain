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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider


class VerifyActivity : AppCompatActivity() {
    val context: Context = this

    private lateinit var otpCode: String
    lateinit var  mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mAuth = FirebaseAuth.getInstance()

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
            val verificationId = intent.getStringExtra("mVerificationId")
            otpCode = etOtpBox1.text.toString() + etOtpBox2.text.toString() + etOtpBox3.text.toString() + etOtpBox4.text.toString() + etOtpBox5.text.toString() + etOtpBox6.text.toString()
            if (otpCode.length == 6) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    verificationId.toString(), otpCode)
                signInWithPhoneAuthCredential(credential)
            } else {
                Toast.makeText(context, "Invalid Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(context, VoterMainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(context, "${task.exception}", Toast.LENGTH_SHORT).show()
                }
            }
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