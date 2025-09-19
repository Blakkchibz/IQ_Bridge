package com.example.iqbridge.quiz

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.iqbridge.activities.QuizActivity
import com.example.iqbridge.adapters.GridAdapter
import com.example.iqbridge.models.QuestionStats
import com.example.iqbridge.models.QuizResponse
import com.example.iqbridge.retrofit.QuestionStatsService
import com.example.iqbridge.retrofit.QuizService
import com.example.iqbridge.utils.Base
import com.example.iqbridge.utils.Constants
import com.example.iqbridge.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class QuizClass(
    private val context: Context
) {
    fun getQuizList(amount:Int, category: Int?, difficulty: String?, type:String?)
    {

        if (Constants.isNetworkAvailable(context))
        {
            val pbDialog = Utils.progressBar(context)

            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service : QuizService = retrofit.create(QuizService::class.java)

            val dataCall : Call<QuizResponse> = service.getQuiz(
                amount, category, difficulty, type
            )

            dataCall.enqueue(object : Callback<QuizResponse> {
                override fun onResponse(
                    call: Call<QuizResponse>,
                    response: Response<QuizResponse>
                ) {
                    pbDialog.cancel()
                    if (response.isSuccessful){
                        val responseData: QuizResponse = response.body()!!
                        val questionList = ArrayList(responseData.results)
                        if (questionList.isNotEmpty()){
                            val intent = Intent(context, QuizActivity::class.java)
                            intent.putExtra("questionList", questionList)
                            context.startActivity(intent)
                        }
                    }
                    else{
                        Utils.showToast(context, "Response Failed")
                    }
                }

                override fun onFailure(
                    p0: Call<QuizResponse?>,
                    p1: Throwable
                ) {
                    pbDialog.cancel()
                    Utils.showToast(context, "Failure in response")
                }
            })
        }
        else{
            Utils.showToast(context, "Network is not available")

        }
    }

    fun setRecyclerView(recycleView:RecyclerView?)
    {
        if (Constants.isNetworkAvailable(context))
        {
            val pbDialog = Base.showProgressBar(context)
            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service:QuestionStatsService = retrofit.create(QuestionStatsService::class.java)
            val dataCall:Call<QuestionStats> = service.getData()
            dataCall.enqueue(object :Callback<QuestionStats>{
                override fun onResponse(
                    call: Call<QuestionStats>,
                    response: Response<QuestionStats>
                ) {
                    pbDialog.cancel()
                    if (response.isSuccessful)
                    {
                        val questionStats:QuestionStats = response.body()!!
                        val categoryMap = questionStats.categories
                        val adapter = GridAdapter(Constants.getCategoryItemList(), categoryMap)
                        recycleView?.adapter = adapter
                        adapter.setOnClickListener(object :GridAdapter.OnClickListener{
                            override fun onClick(id: Int) {
                                getQuizList(10,id,null,null)
                            }

                        })

                    }
                    else
                    {
                        Base.showToast(context, "Error Code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<QuestionStats>, t: Throwable) {
                    pbDialog.cancel()
                    Base.showToast(context,"Network is not Available")
                }

            })
        }
    }
}