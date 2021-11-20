package com.electchain.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.electchain.R
import com.electchain.adapters.CandidateAdapter
import com.electchain.adapters.VotingAdapter
import com.electchain.models.ItemsViewModelCandidate
import com.electchain.models.ItemsViewModelVoting

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

        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        val recyclerViewVoting = findViewById<RecyclerView>(R.id.recyclerViewVoting)
        recyclerViewVoting.layoutManager = LinearLayoutManager(this)

        if (!flag) {
            data.add(ItemsViewModelVoting("0", R.drawable.ic_person, "Paritosh Joshi"))
            data.add(ItemsViewModelVoting("1", R.drawable.ic_person, "Nisarg Dave"))
            data.add(ItemsViewModelVoting("2", R.drawable.ic_person, "Neev Shah"))
            flag = true
        }

        adapter = VotingAdapter(data)
        recyclerViewVoting.adapter = adapter
    }
}