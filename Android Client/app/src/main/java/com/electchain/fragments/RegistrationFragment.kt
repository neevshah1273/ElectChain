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
import com.auth0.android.jwt.JWT
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

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())

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
            mobileNumber = "91$mobileNumber"
            val emailAddress = view.findViewById<EditText>(R.id.registerEmailAddress).text.toString()
            val voterId = view.findViewById<EditText>(R.id.registerVoterId).text.toString()
            
            if (checkValidation(firstName, lastName, dob, mobileNumber, emailAddress, voterId)) {
                val user = User()
                user.firstName = firstName
                user.lastName = lastName
                user.dob = dob
                user.phone = mobileNumber
                user.email = emailAddress
                user.voterId = voterId
                user.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
                registerUser(user)
            } else {
                Toast.makeText(requireActivity(), "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(user: User) {
        val call: Call<Result> = routerService.addUser(user)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result = response.body()!!
                    Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show()
                    emptyEditText()
                } else if (response.code() == 400) {
                    val result = response.body()!!
                    Toast.makeText(requireActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(context, "Server not responding.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun emptyEditText() {
        view?.findViewById<EditText>(R.id.registerFirstName)?.setText("")
        view?.findViewById<EditText>(R.id.registerLastName)?.setText("")
        view?.findViewById<EditText>(R.id.registerDateOfBirth)?.setText("")
        view?.findViewById<EditText>(R.id.registerEmailAddress)?.setText("")
        view?.findViewById<EditText>(R.id.registerMobileNumber)?.setText("")
        view?.findViewById<EditText>(R.id.registerVoterId)?.setText("")
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