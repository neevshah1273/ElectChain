package com.electchain.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.se.omapi.Session
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.activities.MainActivity
import com.electchain.models.Token
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VoterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec")

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
        return inflater.inflate(R.layout.fragment_voter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())
        getVoterDetail()
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    private fun getVoterDetail() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
        val call: Call<User> = routerService.voterDetail(token)

        call.enqueue(object: Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                when {
                    response.code() == 200 -> {
                        val result: User = response.body()!!
                        view?.findViewById<TextView>(R.id.firstName)?.text = result.firstName
                        view?.findViewById<TextView>(R.id.lastName)?.text = result.lastName
                        view?.findViewById<TextView>(R.id.dob)?.text = result.dob
                        view?.findViewById<TextView>(R.id.mobileNumber)?.text = result.phone
                        view?.findViewById<TextView>(R.id.emailAddress)?.text = result.email
                        view?.findViewById<TextView>(R.id.voterId)?.text = result.voterId
                    }
                    response.code() == 400 -> {
                        Toast.makeText(requireActivity(), "Something went wrong. Cannot fetch user details.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(requireActivity(), "Access Denied.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 403 -> {
                        Toast.makeText(requireActivity(), "Invalid Token.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }

        })
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VoterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun logout() {
        sessionManager = SessionManager(requireActivity())
        sessionManager.deleteAuthToken()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}