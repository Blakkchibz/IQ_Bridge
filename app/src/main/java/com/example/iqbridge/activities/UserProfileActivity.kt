package com.example.iqbridge.activities

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.iqbridge.adapters.EditProfileAdapter
import com.example.iqbridge.databinding.ActivityUserProfileBinding
import com.example.iqbridge.firebase.FireBaseClass
import com.example.iqbridge.models.UserModel
import com.example.iqbridge.utils.Constants
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class UserProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserProfileBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FireBaseClass().getUserInfo(object :FireBaseClass.UserInfoCallback{
            override fun onUserInfoFetched(userInfo: UserModel?) {
                binding.tvUserPoints.text = String.format("%.2f",userInfo?.allTimeScore)
                binding.tvLastGameScore.text = String.format("%.2f",userInfo?.lastGameScore)
                binding.tvWeeklyScore.text = String.format("%.2f",userInfo?.weeklyScore)
                binding.tvMonthlyScore.text = String.format("%.2f",userInfo?.monthlyScore)
                binding.tvEmailId.text = userInfo?.emailId
                binding.tvUserName.text = userInfo?.name

                FireBaseClass().setProfileImage(userInfo?.image,binding.userProfilePic)
            }

        })

        updateScoreView(Constants.allTimeScore,binding.tvUserRanking)
        updateScoreView(Constants.weeklyScore,binding.tvWeeklyRank)
        updateScoreView(Constants.monthlyScore,binding.tvMonthlyRank)

        auth = Firebase.auth
        binding.cvSignOut.setOnClickListener {
            if (auth.currentUser!= null)
            {
                auth.signOut()
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.cvEditProfile.setOnClickListener {
            val bottomSheetFragment = EditProfileAdapter(this)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }

    private fun updateScoreView(type:String,textView: TextView){
        FireBaseClass().getUserRank(type,
            object :FireBaseClass.UserRankCallback{
                override fun onUserRankFetched(rank: Int?) {
                    if (rank!=null)
                        textView.text = rank.toString()
                }

            })
    }
}