package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    // use lateinit = promise to compiler will provide non null values later
    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView

    // using lazy makes this prop a val (constant) not a var
    // quizViewModel is assigned a value only once, when it's accessed for 1st time
    private val quizViewModel: QuizViewModel by lazy {
        // ViewModelProvider = registry of ViewModels
        // VMP returns VM instance that was initially created, even after config change
        // onDestroy(), the VM-Activity is removed only if activity.isFinishing() is true
        //    i.e. during rotation/config change, activity is destroyed, but VM is simply disassociated
        //
        // instantiate ViewModel and associate/scope it with MainActivity
        // and get instance of QuizViewModel
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }





    // bundle may be null, e.g. first launch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)


        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0)?:0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)

        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        cheatButton.setOnClickListener { view: View ->
            // Start CheatActivity
            // explicit: packageContext + Class object, to start activity within the same app
            // implicit: app want to start an activity in ANOTHER app

            // google Qualified this
            val intent = CheatActivity.newIntent(this@MainActivity, quizViewModel.currentQuestionAnswer)

            // use ForResult to get response from child
            // request code is a user-defined Int, sent to child, and send back to parent
            //     to identify which child is reporting back (a parent activity can start many child)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.isCheater =
                data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this,
                messageResId,
                Toast.LENGTH_SHORT)
                .show()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    // always called after onStop, survive process death & config change
    // use Bundle to store small, temp data belonging to current activity
    //    but use onStop to save permanent data, since app may be killed by OS on stopped state
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        // save data to Bundle, which is put to activity's activity record by the OS
        // activity record is stored until activity finishes

        super.onSaveInstanceState(savedInstanceState)
        Log.d(TAG, "CALLING onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}