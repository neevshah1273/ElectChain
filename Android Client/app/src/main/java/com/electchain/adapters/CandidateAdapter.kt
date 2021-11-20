package com.electchain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.electchain.R
import com.electchain.models.ItemsViewModelCandidate

class CandidateAdapter(private val mList: List<ItemsViewModelCandidate>) : RecyclerView.Adapter<CandidateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.candidate_cardview_design, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = mList[position]
        holder.ivCandidateProfilePhoto.setImageResource(itemsViewModel.image)
        holder.tvCandidateName.text = itemsViewModel.candidateName
        holder.tvCampaignDescription.text = itemsViewModel.candidateCampaignDescription
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivCandidateProfilePhoto: ImageView = itemView.findViewById(R.id.ivCandidateProfilePhoto)
        val tvCandidateName: TextView = itemView.findViewById(R.id.tvCandidateName)
        val tvCampaignDescription: TextView = itemView.findViewById(R.id.tvCampaignDescription)
    }

}