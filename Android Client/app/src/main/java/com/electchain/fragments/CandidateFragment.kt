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
import com.electchain.R
import com.electchain.adapters.CandidateAdapter
import com.electchain.models.Candidate
import com.electchain.models.ItemsViewModelCandidate
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CandidateFragment : Fragment() {

    private var flag: Boolean = false

    val data = ArrayList<ItemsViewModelCandidate>()
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
        val recyclerViewCandidate = view.findViewById<RecyclerView>(R.id.recyclerViewCandidate)
        recyclerViewCandidate.layoutManager = LinearLayoutManager(context)
        if (!flag) {
            addCandidateToView()
            flag = true
        }
        if (flag) {
            adapter = CandidateAdapter(data)
            recyclerViewCandidate.adapter = adapter
        }
    }

    private fun addCandidateToView() {
        val database = FirebaseDatabase.getInstance("https://electchain-79613-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        database.child("candidates").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    data.add(ItemsViewModelCandidate(
                        R.drawable.ic_person,
                        "${item.child("candidateName").value}",
                        "${item.child("campaignDescription").value}")
                    )
                }
                adapter = CandidateAdapter(data)
                view?.findViewById<RecyclerView>(R.id.recyclerViewCandidate)?.adapter = adapter
            }

            override fun onCancelled(e: DatabaseError) {
                Toast.makeText(requireActivity(),e.message,Toast.LENGTH_SHORT).show()
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
