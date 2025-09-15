package com.example.iqbridge.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.iqbridge.adapters.GridAdapter
import com.example.iqbridge.databinding.ActivityMainBinding
import com.example.iqbridge.models.Category
import com.example.iqbridge.utils.Constants
import com.example.iqbridge.utils.QuizClass

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val quizClass = QuizClass(this)
        val rvCategoryList = binding.rvCategoryList
        rvCategoryList.layoutManager = GridLayoutManager(this, 2)



        quizClass.getQuestionStatsList(object : QuizClass.QuestionStatCallback{
            override fun onQuestionStatFetched(map: Map<String, Category>) {
                val adapter = GridAdapter(Constants.getCategoryItemList(), map)
                rvCategoryList.adapter = adapter
                adapter.setTouchResponse(object : GridAdapter.TouchResponse{
                    override fun onClick(id: Int) {
                        quizClass.getQuizList(10, id, null, null)
                    }
                })

            }
        })

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