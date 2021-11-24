package com.electchain.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.electchain.R
import com.electchain.activities.MainActivity
import com.electchain.activities.VoterLoginActivity
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VoterFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    val months = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "July", "Aug", "Sep", "Oct", "Nov", "Dec")

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
        return inflater.inflate(R.layout.fragment_voter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchUserData()
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun fetchUserData() {
        val database = FirebaseDatabase.getInstance("https://electchain-79613-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users")
        val user = FirebaseAuth.getInstance().currentUser?.phoneNumber
        database.child(user!!).get().addOnSuccessListener {
            if (it.exists()) {
                view?.findViewById<TextView>(R.id.firstName)?.text =
                    it.child("firstName").value as CharSequence?
                view?.findViewById<TextView>(R.id.lastName)?.text =
                    it.child("lastName").value as CharSequence?
                val dob = it.child("dob").value.toString().split('/')
                view?.findViewById<TextView>(R.id.dob)?.text = "${dob[0]}  ${months[dob[1].toInt() - 1]} , ${dob[2]}"
                view?.findViewById<TextView>(R.id.mobileNumber)?.text =
                    it.child("mobileNumber").value as CharSequence?
                view?.findViewById<TextView>(R.id.emailAddress)?.text =
                    it.child("emailAddress").value as CharSequence?
                view?.findViewById<TextView>(R.id.voterId)?.text =
                    it.child("voterId").value as CharSequence?

            } else {
                Toast.makeText(requireActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            VoterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun logout() {
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signOut()
        startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
    }
}