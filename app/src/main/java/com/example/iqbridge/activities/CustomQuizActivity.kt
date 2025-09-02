package com.example.iqbridge.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.iqbridge.databinding.ActivityCustomQuizBinding

class CustomQuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startCustomQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

    }
}