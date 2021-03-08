package com.bignerdranch.android.geoquiz2

import android.util.Log
import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"
private const val MAX_CHEAT_COUNT: Int = 3;

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
    private var cheatedArr: BooleanArray = BooleanArray(questionBank.size){false}
    private var cheatCount: Int = 0

    var currentIndex = 0
    var isCheater = false

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    var cheatedCurrentQuestion: Boolean
        get() = cheatedArr[currentIndex]
        set(value) {
            if (!cheatedArr[currentIndex]) {
                cheatCount++
            }
            cheatedArr[currentIndex] = true
        }

    val canStillCheat: Boolean
        get() = cheatCount < MAX_CHEAT_COUNT


    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}