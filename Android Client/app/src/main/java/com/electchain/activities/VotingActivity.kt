package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.adapters.VotingAdapter
import com.electchain.models.Candidate
import com.electchain.models.ItemsViewModelVoting
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

class VotingActivity : AppCompatActivity() {

    private var flag: Boolean = false

    val data = ArrayList<ItemsViewModelVoting>()
    lateinit var adapter: VotingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voting)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        routerService = retrofit.create(RouterService::class.java)
        sessionManager = SessionManager(applicationContext)
        getCandidateList()
        val recyclerViewVoting: RecyclerView = findViewById(R.id.recyclerViewVoting)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
        recyclerViewVoting.layoutManager = LinearLayoutManager(this)

        if (!flag) {
            flag = true
        }
        adapter = VotingAdapter(data)
        recyclerViewVoting.adapter = adapter
    }

    private fun getCandidateList() {
        val token = Token()
        token.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()

        val call: Call<List<Candidate>> = routerService.getCandidatesList(token)
        call.enqueue(object: Callback<List<Candidate>> {
            override fun onResponse(call: Call<List<Candidate>>, response: Response<List<Candidate>>) {
                if (response.code() == 200) {
                    val result = response.body()!!
                    for (i in result.indices) {
                        data.add(ItemsViewModelVoting(result[i].candidateId, R.drawable.ic_person, result[i].candidateName))
                    }
                    adapter = VotingAdapter(data)
                    findViewById<RecyclerView>(R.id.recyclerViewVoting).adapter = adapter
                } else if (response.code() == 400) {
                    Toast.makeText(applicationContext, "Something went wrong. Cannot load candidate list.", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<Candidate>>, t: Throwable) {
                Toast.makeText(applicationContext, "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}