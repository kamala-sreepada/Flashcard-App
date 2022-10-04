package com.example.someapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashcardQuestion = findViewById<TextView>(R.id.flashcard_question)
        val flashcardAnswer = findViewById<TextView>(R.id.flashcard_answer)
        flashcardQuestion.setOnClickListener {
            flashcardQuestion.visibility = View.INVISIBLE
            flashcardAnswer.visibility = View.VISIBLE
        }
        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->

            val data: Intent? = result.data

            if (data != null) {
                val questionString = data.getStringExtra("QUESTION_KEY")
                val answerString = data.getStringExtra("ANSWER_KEY")
                flashcardQuestion.text = questionString
                flashcardAnswer.text = answerString
                Log.i("Kamala: MainActivity" , "question: $questionString")
                Log.i("Kamala: MainActivity" , "question: $answerString")
            }else{
                Log.i("Kamala: MainActivity","Returned null data from AddCardActivity ")

            }
        }

        //val addQuestionButton = findViewById<ImageView>(R.id.add_question_button)
        //addQuestionButton.setOnClickListener {
          //  val intent = Intent(this , AddCardActivity::class.java)
            //resultLauncher.launch(intent)
        //}
        val addQuestionButton = findViewById<ImageView>(R.id.add_question_button).setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            startActivity(intent)
        }

    }
}

