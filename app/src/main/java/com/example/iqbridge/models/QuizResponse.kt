package com.example.iqbridge.models

data class QuizResponse(
    val response_code: Int,
    val results: List<Result>
)

data class Result(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
)
