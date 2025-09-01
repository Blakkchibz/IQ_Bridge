package com.example.iqbridge.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.starforge.iqbridge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainProfileImage.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnCustomQuiz.setOnClickListener {
            val intent = Intent(this, CustomQuizActivity::class.java)
            startActivity(intent)
        }

        binding.btnRandomQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        binding.ivRankingIcon.setOnClickListener {
            val intent = Intent(this, LeaderBoardActivity::class.java)
            startActivity(intent)
        }

        binding.ivPointsIcon.setOnClickListener {
            val intent = Intent(this, ResultActivity::class.java)
            startActivity(intent)
        }
    }
}