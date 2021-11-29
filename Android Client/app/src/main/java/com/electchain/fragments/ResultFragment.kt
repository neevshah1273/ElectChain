package com.electchain.fragments

import android.opengl.Visibility
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
import com.electchain.models.Candidate
import com.electchain.models.Result
import com.electchain.models.Status
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

class ResultFragment : Fragment() {
    private var isActive = false
    private var isCompleted = false
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
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())
        checkStatus()
        if (!isCompleted) {
            view.findViewById<TextView>(R.id.tvText).visibility = View.GONE
        } else {
            getResult()
        }
        view.findViewById<Button>(R.id.btnCheckResult).setOnClickListener {
            if (isCompleted) {
                getResult()
            } else {
                Toast.makeText(requireActivity(), "Voting hasn't ended yet.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getResult() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
        val call: Call<Result> = routerService.getResult(token)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result = response.body()!!
                    view?.findViewById<TextView>(R.id.tvText)?.visibility = View.VISIBLE
                    view?.findViewById<TextView>(R.id.tvResult)?.text = result.message
                } else if (response.code() == 400) {
                    Toast.makeText(requireActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkStatus() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
        val call: Call<Status> = routerService.isActive(token)
        call.enqueue(object: Callback<Status> {
            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.code() == 200) {
                    val result: Status = response.body()!!
                    isActive = result.isActive
                    isCompleted = result.isCompleted
                } else {
                    Toast.makeText(requireActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Status>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}