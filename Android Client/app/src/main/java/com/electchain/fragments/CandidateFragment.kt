package com.electchain.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electchain.R
import com.electchain.adapters.CandidateAdapter
import com.electchain.models.ItemsViewModelCandidate

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
        if (!flag) {
            data.add(ItemsViewModelCandidate(R.drawable.ic_person, "Paritosh Joshi", "This is my campaign description. Vote for me dummies."))
            data.add(ItemsViewModelCandidate(R.drawable.ic_person, "Nisarg Dave", "This is my campaign description. Vote for me dummies."))
            data.add(ItemsViewModelCandidate(R.drawable.ic_person, "Neev Shah", "This is my campaign description. Vote for me dummies."))
            flag = true
        }
        val recyclerViewCandidate = view.findViewById<RecyclerView>(R.id.recyclerViewCandidate)
        recyclerViewCandidate.layoutManager = LinearLayoutManager(context)
        adapter = CandidateAdapter(data)
        recyclerViewCandidate.adapter = adapter
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
