package com.example.timefighter

import android.content.IntentSender
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    internal var gameStarted = false

    internal lateinit var countDownTimer: CountDownTimer
    internal var initialCountDown: Long = 60000
    internal var timeLeft = 60
    internal var countDownInterval: Long = 1000

    companion object{
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"


    }




    internal lateinit var gameScoreTextView: TextView
    internal lateinit var timeLeftTextView: TextView
    internal lateinit var tapMeButton: Button
    internal var score = 0

    internal val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "OnCreate called: $score" )

        gameScoreTextView = findViewById(R.id.game_score_text_view)
        timeLeftTextView = findViewById(R.id.time_left_text_view)
        tapMeButton = findViewById(R.id.tap_me_button)

        if(savedInstanceState != null){
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getInt(TIME_LEFT_KEY)
            restoreGame()
        }else{
            resetGame()
        }
        tapMeButton.setOnClickListener{v ->

            val bounceAnimation = AnimationUtils.loadAnimation(this,R.anim.bounce)
            v.startAnimation(bounceAnimation)


            incrementScore()}


    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putInt(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d(TAG, "onSavedInstance: Score is $score timeLeft is $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy is called")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_settings){
            showInfo()
        }
        return true
    }


    private fun incrementScore(){
        score++

        val newScore = "Your Score: $score"
        gameScoreTextView.text = newScore

        if(!gameStarted){
            startGame()
        }

    }
    private fun resetGame(){
        score = 0

        val initialScore = getString(R.string.your_score, score.toString())
        gameScoreTextView.text = initialScore

        val initialTimeLeft = getString(R.string.time_left, timeLeft.toString())
        timeLeftTextView.text  = initialTimeLeft

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt()/1000

                val timeLeftString = getString(R.string.time_left, timeLeft.toString())
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false




    }
    private fun restoreGame(){

        val restoredScore = getString(R.string.your_score, score.toString())
        gameScoreTextView.text = restoredScore

        val restoredTime = getString(R.string.time_left, timeLeft.toString())
        timeLeftTextView.text = restoredTime

        countDownTimer = object: CountDownTimer((timeLeft)*1000.toLong()/1000, countDownInterval){
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished.toInt()/1000

                var timeLeftString = getString(R.string.time_left, timeLeft.toString())
                timeLeftTextView.text = timeLeftString
            }

            override fun onFinish() {
               endGame()
            }



        }

        countDownTimer.start()
        gameStarted = true



    }
    private fun startGame(){
        countDownTimer.start()
        gameStarted = true

    }
    private fun endGame(){
        Toast.makeText(this, getString(R.string.game_over_message, score.toString()), Toast.LENGTH_LONG).show()
        resetGame()

    }
    private fun showInfo(){
        val dialogTitle = getString(R.string.about_title, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.about_message)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("TimeFighter 2.0")
        builder.setMessage(dialogMessage)
        builder.create().show()
    }
}