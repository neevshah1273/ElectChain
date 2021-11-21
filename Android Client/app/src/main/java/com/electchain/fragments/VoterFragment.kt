package com.electchain.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.electchain.R
import com.electchain.activities.VoterLoginActivity
import com.electchain.utils.Constants.sessionManager
import com.electchain.utils.SessionManager

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class VoterFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_voter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logout()
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
        sessionManager = activity?.let { SessionManager(it.applicationContext) }!!
        sessionManager.deleteAuthToken()
        startActivity(Intent(activity, VoterLoginActivity::class.java))
        requireActivity().finish()
    }
}