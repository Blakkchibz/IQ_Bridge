package com.example.iqbridge.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iqbridge.databinding.ActivityMainBinding
import com.example.iqbridge.firebase.FireBaseClass
import com.example.iqbridge.models.UserModel
import com.example.iqbridge.utils.Constants
import com.example.iqbridge.quiz.QuizClass

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FireBaseClass().getUserInfo(object :FireBaseClass.UserInfoCallback{
            override fun onUserInfoFetched(userInfo: UserModel?) {
                binding.tvUserPoints.text = String.format("%.2f",userInfo?.allTimeScore)
                binding.tvUserName.text = "Hi, " + userInfo?.name
                FireBaseClass().setProfileImage(userInfo?.image,binding.mainProfileImage)
            }
        })

        FireBaseClass().getUserRank(Constants.allTimeScore,
            object :FireBaseClass.UserRankCallback{
                override fun onUserRankFetched(rank: Int?) {
                    if (rank!=null)
                        binding.tvUserRanking.text = rank.toString()
                }

            })

        val rvCategoryList = binding.rvCategoryList
        rvCategoryList.layoutManager = GridLayoutManager(this,2)
        val quizClass = QuizClass(this)
        quizClass.setRecyclerView(rvCategoryList)

        binding.btnRandomQuiz.setOnClickListener {
            quizClass.getQuizList(10,null,null,null)
        }

        binding.btnCustomQuiz.setOnClickListener {
            startActivity(Intent(this,CustomQuizActivity::class.java))
        }

        binding.cvPlayerStats.setOnClickListener {
            startActivity(Intent(this,LeaderBoardActivity::class.java))
        }

        binding.mainProfileImage.setOnClickListener {
            startActivity(Intent(this, UserProfileActivity::class.java))
        }


    }
}