package com.electchain.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.electchain.R
import com.electchain.models.Candidate
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnSubmit).setOnClickListener {
            val etCandidateName = view.findViewById<EditText>(R.id.etCandidateName).text.toString()
            val etCampaignDescription = view.findViewById<EditText>(R.id.etCampaignDescription).text.toString()

            if (checkValidation(etCandidateName, etCampaignDescription)) {
                val databaseReference = FirebaseDatabase.getInstance().getReference("Candidates")
                val candidate = Candidate(etCandidateName, etCampaignDescription)
                val id = databaseReference.push().key
                Log.d("TAG", "id: $id")
                databaseReference.child(id!!).setValue(candidate).addOnSuccessListener {
                    Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(requireActivity(), "Failure", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireActivity(), "All fields are required.", Toast.LENGTH_SHORT).show()
            }
        }
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