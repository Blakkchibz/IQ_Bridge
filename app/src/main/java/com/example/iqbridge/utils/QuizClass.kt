package com.example.iqbridge.utils

import android.content.Context
import android.content.Intent
import com.example.iqbridge.activities.QuizActivity
import com.example.iqbridge.models.Category
import com.example.iqbridge.models.QuestionsStats
import com.example.iqbridge.models.QuizResponse
import com.example.iqbridge.retrofit.QuestionStatsService
import com.example.iqbridge.retrofit.QuizService
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

            dataCall.enqueue(object : Callback<QuizResponse>{
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

    fun getQuestionStatsList(callBack: QuestionStatCallback){
        if (Constants.isNetworkAvailable(context))
        {
            val pbDialog = Utils.progressBar(context)
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://opentdb.com/")
                .addConverterFactory(GsonConverterFactory.create()).build()

            val service:QuestionStatsService = retrofit.create(QuestionStatsService::class.java)
            val dataCall:Call<QuestionsStats> = service.getData()

            dataCall.enqueue(object  : Callback<QuestionsStats>{
                override fun onResponse(
                    call: Call<QuestionsStats>,
                    response: Response<QuestionsStats>
                ){
                    pbDialog.cancel()
                    if (response.isSuccessful){
                        val questionsStats: QuestionsStats = response.body()!!
                        val categoryMap = questionsStats.categories
                        callBack.onQuestionStatFetched(categoryMap)
                        //TODO Yet to implement
                    }
                    else{
                        Utils.showToast(context, "Error Code: ${response.code()}")
                    }
                }

                override fun onFailure(
                    p0: Call<QuestionsStats?>,
                    t: Throwable
                ) {
                    pbDialog.cancel()
                    Utils.showToast(context, "API Call Failure")
                }
            })
        }
        else{
            Utils.showToast(context,"Network is Not Available")
        }
    }

    interface QuestionStatCallback{
        fun onQuestionStatFetched(map: Map<String, Category>)
    }
}