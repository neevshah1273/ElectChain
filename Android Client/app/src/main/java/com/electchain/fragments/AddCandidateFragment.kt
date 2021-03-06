package com.electchain.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.models.Candidate
import com.electchain.models.Result
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

class AddCandidateFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_add_candidate, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())

        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val etCandidateName = view.findViewById<EditText>(R.id.etCandidateName).text.toString()
            val etCampaignDescription = view.findViewById<EditText>(R.id.etCampaignDescription).text.toString()
            if (checkValidation(etCandidateName, etCampaignDescription)) {
                val candidate = Candidate()
                candidate.candidateName = etCandidateName
                candidate.campaignDescription = etCampaignDescription
                candidate.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
                addCandidate(candidate)
            } else {
                Toast.makeText(requireActivity(), "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCandidate(candidate: Candidate) {
        val call: Call<Result> = routerService.addCandidate(candidate)

        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                if (response.code() == 200) {
                    val result: Result = response.body()!!
                    Toast.makeText(requireActivity(), result.message, Toast.LENGTH_SHORT).show()
                    emptyEditText()
                } else if (response.code() == 400) {
                    Toast.makeText(requireActivity(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun emptyEditText() {
        view?.findViewById<EditText>(R.id.etCandidateName)?.setText("")
        view?.findViewById<EditText>(R.id.etCampaignDescription)?.setText("")
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCandidateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun checkValidation(etCandidateName: String, etCampaignDescription: String): Boolean {
        return etCandidateName.isNotEmpty() && etCampaignDescription.isNotEmpty()
    }
}