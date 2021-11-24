package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electchain.R
import com.electchain.adapters.CandidateAdapter
import com.electchain.adapters.VotingAdapter
import com.electchain.models.ItemsViewModelCandidate
import com.electchain.models.ItemsViewModelVoting
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

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
        val recyclerViewVoting: RecyclerView = findViewById(R.id.recyclerViewVoting)
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }
        recyclerViewVoting.layoutManager = LinearLayoutManager(this)

        if (!flag) {
            addCandidates()
            flag = true
        }

        adapter = VotingAdapter(data)
        recyclerViewVoting.adapter = adapter
    }

    private fun addCandidates() {
        val database = FirebaseDatabase.getInstance("https://electchain-79613-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
        database.child("candidates").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (item in snapshot.children) {
                    data.add(ItemsViewModelVoting(
                        "${item.child("uuid").value}",
                        R.drawable.ic_person,
                        "${item.child("candidateName").value}")
                    )
                }
                adapter = VotingAdapter(data)
                findViewById<RecyclerView>(R.id.recyclerViewVoting).adapter = adapter
            }

            override fun onCancelled(e: DatabaseError) {
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}