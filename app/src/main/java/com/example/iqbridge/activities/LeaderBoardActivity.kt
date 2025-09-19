package com.example.iqbridge.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iqbridge.R
import com.example.iqbridge.adapters.LeaderBoardAdapter
import com.example.iqbridge.databinding.ActivityLeaderBoardBinding
import com.example.iqbridge.firebase.FireBaseClass
import com.example.iqbridge.models.LeaderBoardModel
import com.example.iqbridge.utils.Base
import com.example.iqbridge.utils.Constants

class LeaderBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set RecyclerView layout manager
        binding.leaderBoardRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load leaderboard
        setLeaderBoard(Constants.allTimeScore)

        // RadioGroup listener
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbAllTime -> setLeaderBoard(Constants.allTimeScore)
                R.id.rbWeekly -> setLeaderBoard(Constants.weeklyScore)
                R.id.rbMonthly -> setLeaderBoard(Constants.monthlyScore)
            }
        }
    }

    private fun setLeaderBoard(type: String) {
        FireBaseClass().getLeaderBoardData(type, object : FireBaseClass.LeaderBoardDataCallback {
            override fun onLeaderBoardDataFetched(leaderBoardModel: LeaderBoardModel?) {

                val users = leaderBoardModel?.topUsers ?: emptyList()

                // Top 3 ranks
                val topViews = listOf(
                    Triple(binding.tvRank1Name, binding.tvRank1Points, binding.ivRank1),
                    Triple(binding.tvRank2Name, binding.tvRank2Points, binding.ivRank2),
                    Triple(binding.tvRank3Name, binding.tvRank3Points, binding.ivRank3)
                )

                for (i in 0 until 3) {
                    val (nameView, pointsView, imageView) = topViews[i]
                    val user = users.getOrNull(i)

                    if (user != null) {
                        nameView.text = user.name ?: "N/A"
                        pointsView.text = String.format("%.2f", Base.desiredScore(user, type))
                        FireBaseClass().setProfileImage(user.image, imageView)
                    } else {
                        nameView.text = "N/A"
                        pointsView.text = "0"
                        imageView.setImageResource(R.drawable.profile_pic)
                    }
                }

                // Remaining users for RecyclerView
                val otherUsers = if (users.size > 3) users.subList(3, users.size) else emptyList()
                binding.leaderBoardRecyclerView.adapter = LeaderBoardAdapter(otherUsers, type)
            }
        })
    }
}
