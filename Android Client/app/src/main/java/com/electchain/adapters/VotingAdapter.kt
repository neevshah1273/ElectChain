package com.electchain.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.electchain.R
import com.electchain.activities.MainActivity
import com.electchain.models.ItemsViewModelVoting


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

            val intent = Intent(holder.context, MainActivity::class.java)
            holder.context.startActivity(intent)
            (holder.context as Activity).finish()
        }
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