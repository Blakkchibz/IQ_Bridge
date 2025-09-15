package com.example.iqbridge.activities


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.iqbridge.adapters.QuizSummaryAdapter
import com.example.iqbridge.databinding.ActivityResultBinding
import com.example.quizwiz.models.ResultModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val resultList: ArrayList<ResultModel> = intent.getSerializableExtra("resultList")
            as ArrayList<ResultModel>

        binding.rvSummary.layoutManager = LinearLayoutManager(this)
        val adapter = QuizSummaryAdapter(resultList)
        binding.rvSummary.adapter = adapter

        binding.tvTotalScore.text = getFinalScore(resultList).toString()


        binding.btnHome.setOnClickListener {
            finish()
        }
    }

    private fun getFinalScore(list: ArrayList<ResultModel>): Double {
        var result = 0.0
        for (i in list)
            result += i.score
        return String.format("%.2f",result).toDouble()
    }
}