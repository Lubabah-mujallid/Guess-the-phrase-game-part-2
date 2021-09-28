package com.example.guessphrasegame

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var myCL: ConstraintLayout
    private lateinit var myET: EditText
    private lateinit var myButton: Button
    private lateinit var mylist: ArrayList<String>
    private lateinit var myPhraseTV: TextView
    private lateinit var myLettersTV: TextView
    private lateinit var myHighestScore: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private val originalPhrase = "Banana banana"
    private var numberOfGuesses = 10
    private var starPhrase = stringToStars(originalPhrase).toCharArray()
    private var isPhrase = true
    private var highScore = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("HighScore", 0)
        myHighestScore = findViewById(R.id.tvHighestScore)
        var temp = "Highest Score: $highScore"
        myHighestScore.text = temp

        myCL = findViewById(R.id.clRoot)
        mylist = ArrayList()
        myET = findViewById(R.id.etEntry)
        myButton = findViewById(R.id.okButton)
        myPhraseTV = findViewById(R.id.tvPhrase)
        myLettersTV = findViewById(R.id.tvLetters)
        rvList.adapter = MessageAdapter(this, mylist)
        rvList.layoutManager = LinearLayoutManager(this)

        var demo = "The Phrase is: ${String(starPhrase)}"
        myPhraseTV.text = demo

        myButton.setOnClickListener {
            val guess = myET.text.toString()
            if (guess.isNotEmpty()) {
                if (isPhrase) guessPhrase(guess)
                else guessLetter(guess)
                myET.text.clear()
                myET.clearFocus()
                rvList.adapter?.notifyDataSetChanged()
                updateHighestScore()
            }else{
                Snackbar.make(myCL, "Please enter at least a letter", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    /*
        fun guessPhraseGame() { //keywords: Congrats - Tsk
            var guess = myET.text.toString()
            myET.text.clear()
            myET.clearFocus()
            rvList.adapter?.notifyDataSetChanged()
            mylist.add("Your guess is: $guess")
            if (guess.equals(originalPhrase, ignoreCase = true) ) {
                disableEntry()
                mylist.add("Congrats!! You Win.")
            } //win the game, if the full phrase was correct
            else { //phrase is wrong
                numberOfGuesses--
                mylist.add("Tsk! Wrong guess.")

                guess = ""
                //user should guess a letter
                myET.hint = "Enter a Letter"
                myButton.setOnClickListener { guess = myET.text.toString() }
                myET.text.clear()
                myET.clearFocus()
                rvList.adapter?.notifyDataSetChanged()

                //check if letter found in secret phrase
                var foundLetters = 0
                var foundIndices = arrayListOf<Int>()
                for (i in originalPhrase.indices) {
                    if (originalPhrase[i] == guess.first()) {
                        foundIndices.add(i)
                        foundLetters++
                    }
                } //collct found letters
                if(foundLetters != 0){ // found the letter
                    mylist.add("Congrats, $foundLetters letters are found.")

                    //fix stars with new letters
                    for (i in originalPhrase.indices) {
                        if (originalPhrase[i] == guess.first()) {
                            starPhrase[i] = guess.first()
                        }
                    }

                    //if all letters found --> done, stop operations, ask to play again
                    //TODO("make a win function")
                    if (starPhrase.toString() == originalPhrase) {
                        disableEntry()
                        mylist.add("Congrats!! You Win.")
                        TODO()//showAlertDialog("You win!\n\nPlay again?")
                    } //win the game, if the full phrase was correct

                } // found the letter
                else {//letter not found
                    mylist.add("Tsk! letter not found.")
                } //letter not found

                // if 10 tries finished --> stop operations, game over
                if (numberOfGuesses == 0){
                    disableEntry()
                    mylist.add("Tsk!! You Lost.")
                    TODO()//showAlertDialog("You win!\n\nPlay again?")
                }
                // tell user how many guesses remaining
                mylist.add("you have $numberOfGuesses guesses left.")
            } // phrase is wrong

            myET.text.clear()
            myET.clearFocus()
            rvList.adapter?.notifyDataSetChanged()
            myET.hint = "Enter a Phrase"
        }*/
    private fun guessPhrase(guess: String) {
        if (guess.equals(originalPhrase, ignoreCase = true)) {
            disableEntry()
            score = 500
            mylist.add("Congrats!! You Win. Your score is $score")
        } //win the game, if the full phrase was correct
        else { //phrase is wrong
            numberOfGuesses--
            mylist.add("Tsk! Wrong guess.")
        }
        isPhrase = false
        myET.hint = "Enter a Letter"
    }
    private fun guessLetter(guess: String) {
        var foundLetters = 0
        var foundIndices = arrayListOf<Int>()
        for (i in originalPhrase.indices) {
            if (originalPhrase[i].equals(guess.first(), ignoreCase = true)) {
                foundIndices.add(i)
                foundLetters++
            }
        } //collct found letters
        if (foundLetters != 0) { // found the letter
            score += 10
            mylist.add("Congrats, $foundLetters letters are found. your new score is $score")

            //fix stars with new letters
            for (i in originalPhrase.indices) {
                if (originalPhrase[i].equals(guess.first(), ignoreCase = true)) {
                    starPhrase[i] = guess.first().uppercaseChar()
                }
            }
            var demo = myLettersTV.text.toString() + " $guess"
            myLettersTV.text = demo
            //if all letters found --> done, stop operations, ask to play again
            if (String(starPhrase).equals(originalPhrase, ignoreCase = true)) {
                disableEntry()
                mylist.add("Congrats!! You Win.")
            } //win the game, if the full phrase was correct

        } // found the letter
        else {//letter not found
            mylist.add("Tsk! letter not found.")
        } //letter not found

        // if 10 tries finished --> stop operations, game over
        if (numberOfGuesses == 0) {
            disableEntry()
            mylist.add("Tsk!! You Lost.")
        }
        // tell user how many guesses remaining
        var demo = "The Phrase is: ${String(starPhrase)}"
        myPhraseTV.text = demo
        mylist.add("you have $numberOfGuesses guesses left.")
        isPhrase = true
        myET.hint = "Enter a Phrase"
    }
    private fun stringToStars(phrase: String): String {
        var starPhrase = ""
        for (i in phrase) {
            starPhrase += if (i == ' ') ' '
            else '*'
        }
        return starPhrase
    } //take the original phrase and converts each letter into a star character
    private fun disableEntry() {
        myButton.isEnabled = false
        myButton.isClickable = false
        myET.isEnabled = false
        myET.isClickable = false
    }
    private fun updateHighestScore(){
        if(score > highScore){
            highScore = score
            with(sharedPreferences.edit()) {
                putInt("HighScore", highScore)
                apply()
            }
            Snackbar.make(clRoot, "NEW HIGH SCORE! AMAZING!!", Snackbar.LENGTH_LONG).show()
            var temp = "Highest Score: $highScore"
            myHighestScore.text = temp
        }
    }
}



//my logic
/*
take guess of user phrase
repeat 10 times unless correct
    if phrase correct -->
        done, stop operations, ask to play again
    else (phrase wrong) -->
        ask to guess letter
        if letter found in secret phrase -->
            print how many found
            fix star secret with new letters
                for loop to traverse original and compare it with letters found
            if all letters found -->
                done, stop operations, ask to play again
        else (letter not found) -->
            tell user not found
        if 10 tries finished --> stop operations, game over
        tell user how many guesses remaining
* */
