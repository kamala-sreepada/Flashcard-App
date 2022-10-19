package com.example.someapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    var currCardDisplayedIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)

        if (allFlashcards.size > 0) {
            flashcardQuestion.text = allFlashcards[0].question
            flashcardAnswer.text = allFlashcards[0].answer
        }

        flashcardQuestion.setOnClickListener {
            flashcardAnswer.visibility = View.VISIBLE

            flashcardQuestion.visibility = View.INVISIBLE



            Log.i("Paulina", "Question button was clicked")

            val answerSideView = findViewById<View>(R.id.flashcard_answer)
            val questionSideView = findViewById<View>(R.id.flashcard_question)


            val cx = answerSideView.width / 2
            val cy = answerSideView.height / 2


            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            val anim = ViewAnimationUtils.createCircularReveal(answerSideView , cx , cy , 0f , finalRadius)

            questionSideView.visibility = View.INVISIBLE
            answerSideView.visibility = View.VISIBLE

            anim.duration = 2000
            anim.start()
        }

        flashcardAnswer.setOnClickListener {
            flashcardAnswer.visibility = View.INVISIBLE
            flashcardQuestion.visibility = View.VISIBLE
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->

            val data: Intent? = result.data

            if (data != null) {
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")

                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString

                Log.i("Paulina: MainActivity", "question: $questionString")
                Log.i("Paulina: MainActivity", "answer: $answerString")

                if (!questionString.isNullOrEmpty() && !answerString.isNullOrEmpty()) {
                    flashcardDatabase.insertCard(Flashcard(questionString, answerString))
                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                }
            } else {
                Log.i("Paulina: MainActivity", "Returned null data from AddCardActivity")
            }
        }

        val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)
        addQuestionButton.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_in)
        }

        val nextButton = findViewById<ImageView>(R.id.next_button)
        nextButton.setOnClickListener {
            if (allFlashcards.isEmpty()) {
                // early return so that the rest of the code doesn't execute
                return@setOnClickListener
            }



            val leftOutAnim = AnimationUtils.loadAnimation(it.context, R.anim.left_in)
            val rightInAnim = AnimationUtils.loadAnimation(it.context, R.anim.right_in)

            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // this method is called when the animation first starts
                    flashcardAnswer.visibility = View.INVISIBLE
                    flashcardQuestion.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // this method is called when the animation is finished playing
                    currCardDisplayedIndex++

                    if (currCardDisplayedIndex >= allFlashcards.size) {
                        // go back to the beginning
                        currCardDisplayedIndex = 0
                    }

                    allFlashcards = flashcardDatabase.getAllCards().toMutableList()

                    val question = allFlashcards[currCardDisplayedIndex].question
                    val answer = allFlashcards[currCardDisplayedIndex].answer

                    flashcardQuestion.text = question
                    flashcardAnswer.text = answer

                    flashcardAnswer.visibility = View.INVISIBLE
                    flashcardQuestion.visibility = View.VISIBLE

                }

                override fun onAnimationRepeat(animation: Animation?) {
                    // we don't need to worry about this method
                }
            })
            findViewById<View>(R.id.flashcard_question).startAnimation(leftOutAnim)
        }
    }
}