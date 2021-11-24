package com.electchain.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.electchain.R
import com.electchain.models.Candidate
import com.electchain.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegistrationFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val registerDateOfBirth = view.findViewById<EditText>(R.id.registerDateOfBirth)

        registerDateOfBirth.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(requireActivity(), {
                    view, year, monthOfYear, dayOfMonth ->
                registerDateOfBirth.setText("$dayOfMonth/${monthOfYear + 1}/$year")
            }, year, month, day)
            dpd.show()
        }

        view.findViewById<Button>(R.id.btnRegister).setOnClickListener {
            val firstName = view.findViewById<EditText>(R.id.registerFirstName).text.toString()
            val lastName = view.findViewById<EditText>(R.id.registerLastName).text.toString()
            val dob = registerDateOfBirth.text.toString()
            var mobileNumber = view.findViewById<EditText>(R.id.registerMobileNumber).text.toString()
            val emailAddress = view.findViewById<EditText>(R.id.registerEmailAddress).text.toString()
            val voterId = view.findViewById<EditText>(R.id.registerVoterId).text.toString()
            
            if (checkValidation(firstName, lastName, dob, mobileNumber, emailAddress, voterId)) {
                val ref = FirebaseDatabase.getInstance("https://electchain-79613-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
                mobileNumber = "+91$mobileNumber"
                val user = User(firstName, lastName, dob, mobileNumber, emailAddress, voterId)
                ref.child(mobileNumber).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireActivity(), "User registered successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegistrationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun checkValidation(firstName: String, lastName: String, dob: String, mobileNumber: String, emailAddress: String, voterId: String): Boolean {
        return firstName.isNotEmpty() && lastName.isNotEmpty() && dob.isNotEmpty() && mobileNumber.isNotEmpty() && emailAddress.isNotEmpty() && voterId.isNotEmpty()
    }

}