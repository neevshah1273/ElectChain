package com.electchain.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.models.Result
import com.electchain.models.Token
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

class PhaseFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_phase, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())

        getCurrentPhase()
        view.findViewById<Button>(R.id.btnPhase).setOnClickListener {
            changeCurrentPhase()
        }
    }

    private fun getCurrentPhase() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()

        val call: Call<Result> = routerService.currentPhase(token)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result: Result = response.body()!!
                    view!!.findViewById<TextView>(R.id.tvCurrentPhase)!!.text = result.message

                } else if (response.code() == 400) {
                    Toast.makeText(requireActivity(), "Something went wrong. Cannot get current status.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun changeCurrentPhase() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()

        val call: Call<Result> = routerService.changePhase(token)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result: Result = response.body()!!
                    Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show()

                } else if (response.code() == 400) {
                    Toast.makeText(requireActivity(), "Something went wrong. Cannot change voting phase.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhaseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}