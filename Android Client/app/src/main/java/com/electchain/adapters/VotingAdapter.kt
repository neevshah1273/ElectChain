package com.electchain.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.auth0.android.jwt.JWT
import com.electchain.R
import com.electchain.activities.VoterMainActivity
import com.electchain.models.Candidate
import com.electchain.models.ItemsViewModelVoting
import com.electchain.models.Result
import com.electchain.utils.Constants.routerService
import com.electchain.utils.Constants.sessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VotingAdapter(private val mList: List<ItemsViewModelVoting>): RecyclerView.Adapter<VotingAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview_voting_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val itemsViewModel = mList[position]
        holder.ivCandidateProfilePhoto.setImageResource(itemsViewModel.image)
        holder.tvCandidateName.text = itemsViewModel.candidateName

        holder.tvBtnVote.setOnClickListener {
            Log.d("Card Id", itemsViewModel._id)
            val candidate = Candidate()
            candidate.candidateId = itemsViewModel._id
            candidate.token = sessionManager.fetchAuthToken()?.let { it1 -> JWT(it1) }.toString()
            transferVote(candidate, holder.context)
        }
    }

    private fun transferVote(candidate: Candidate, context: Context) {
        val call: Call<Result> = routerService.transferVote(candidate)
        call.enqueue(object: Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                when {
                    response.code() == 200 -> {
                        val result: Result = response.body()!!
                        Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, VoterMainActivity::class.java)
                        context.startActivity(intent)
                    }
                    response.code() == 400 -> {
                        Toast.makeText(context, "Something went wrong. Could not register your vote.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 401 -> {
                        Toast.makeText(context, "Access Denied.", Toast.LENGTH_SHORT).show()
                    }
                    response.code() == 403 -> {
                        Toast.makeText(context, "Invalid Token.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(context, "Server not responding.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View): RecyclerView.ViewHolder(ItemView) {
        val ivCandidateProfilePhoto: ImageView = itemView.findViewById(R.id.ivCandidateProfilePhoto)
        val tvCandidateName: TextView = itemView.findViewById(R.id.tvCandidateName)
        val tvBtnVote: TextView = itemView.findViewById(R.id.tvBtnVote)
        val context: Context = itemView.context
    }
}