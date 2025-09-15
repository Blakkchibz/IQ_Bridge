package com.example.iqbridge.models

import java.io.Serializable

data class QuizResponse(
    val response_code: Int,
    val results: List<QuestionResult>
)

data class QuestionResult(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    val correct_answer: String,
    val incorrect_answers: List<String>
): Serializable
