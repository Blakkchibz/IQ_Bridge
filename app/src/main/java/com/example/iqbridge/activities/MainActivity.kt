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
import com.example.iqbridge.utils.Utils

class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val quizClass = QuizClass(this)
        val rvCategoryList = binding?.rvCategoryList
        rvCategoryList?.layoutManager = GridLayoutManager(this, 2)

        quizClass.getQuestionStatsList(object : QuizClass.QuestionStatCallback{
            override fun onQuestionStatFetched(map: Map<String, Category>) {
                val adapter = GridAdapter(Constants.getCategoryItemList(), map)
                rvCategoryList?.adapter = adapter

            }
        })


    }
}