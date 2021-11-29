package com.electchain.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.adapters.CandidateAdapter
import com.electchain.models.Candidate
import com.electchain.models.ItemsViewModelCandidate
import com.electchain.models.Token
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

class CandidateFragment : Fragment() {

    private var flag: Boolean = false

    private val data = ArrayList<ItemsViewModelCandidate>()
    lateinit var adapter: CandidateAdapter

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
        return inflater.inflate(R.layout.fragment_candidate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(requireActivity())

        val recyclerViewCandidate = view.findViewById<RecyclerView>(R.id.recyclerViewCandidate)
        recyclerViewCandidate.layoutManager = LinearLayoutManager(context)
        if (!flag) {
            getCandidateList()
            flag = true
        }
        if (flag) {
            adapter = CandidateAdapter(data)
            recyclerViewCandidate.adapter = adapter
        }
    }

    private fun getCandidateList() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()

        val call: Call<List<Candidate>> = routerService.getCandidatesList(token)

        call.enqueue(object: Callback<List<Candidate>> {
            override fun onResponse(call: Call<List<Candidate>>, response: Response<List<Candidate>>) {
                when {
                    response.code() == 200 -> {
                        val result: List<Candidate> = response.body()!!
                        for (i in result.indices) {
                            data.add(ItemsViewModelCandidate(R.drawable.ic_person, result[i].candidateName, result[i].campaignDescription))
                        }
                        adapter = CandidateAdapter(data)
                        view?.findViewById<RecyclerView>(R.id.recyclerViewCandidate)?.adapter = adapter
                    }
                    response.code() == 400 -> {
                        Toast.makeText(requireActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(requireActivity(), "Access Denied.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 403 -> {
                        Toast.makeText(requireActivity(), "Invalid token.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<List<Candidate>>, t: Throwable) {
                Toast.makeText(requireActivity(), "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CandidateFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
