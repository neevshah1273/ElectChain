package com.electchain.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.activities.VotingActivity
import com.electchain.adapters.CandidateAdapter
import com.electchain.models.ItemsViewModelCandidate
import com.electchain.models.Status
import com.electchain.models.Token
import com.electchain.utils.Constants
import com.electchain.utils.Constants.BASE_URL
import com.electchain.utils.Constants.retrofit
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.RouterService
import com.electchain.utils.SessionManager
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Future
import kotlin.properties.Delegates

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ElectionFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_election, container, false)
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
        checkStatus()
        view.findViewById<Button>(R.id.votingBtn).setOnClickListener {
//            if (isActive && !isCompleted) {
//                startActivity(Intent(context, VotingActivity::class.java))
//            } else if (!isActive && !isCompleted) {
//                Toast.makeText(requireActivity(), "Voting hasn't started yet.", Toast.LENGTH_SHORT).show()
//            } else if (!isActive && isCompleted) {
//                Toast.makeText(requireActivity(), "Result has been published.", Toast.LENGTH_SHORT).show()
//            }
            startActivity(Intent(context, VotingActivity::class.java))
        }
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
            ElectionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}