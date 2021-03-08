package com.bignerdranch.android.geoquiz2

import android.util.Log
import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"

// good for dynamic data
class QuizViewModel: ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    private var cheatRecord = BooleanArray(questionBank.size){false}

    var currentIndex = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var cheatedCurrentQuestion: Boolean
        get() = cheatRecord[currentIndex]
        set(value) {
            cheatRecord[currentIndex] = value
        }

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}