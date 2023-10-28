package com.example.tictactoe

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.example.tictactoe.databinding.ActivityMainBinding
import kotlin.random.Random



class MainActivity : AppCompatActivity() {
    private var playerCount = 1 // За замовчуванням гра для одного гравця
    private var currentPlayer = 1 // За замовчуванням починає гравець 1
    private val board = Array(3) { IntArray(3) }
    private var player1Name: String = "Комп'ютер" // За замовчуванням
    private var player2Name: String = "Гравець" // За замовчуванням
    // Початкова установка для визначення, хто починає
    private var isComputerTurn = false // Додаємо змінну для визначення, чи ходить комп'ютер


    private fun startTwoPlayersGameWithNames(player1Name: String, player2Name: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("playerCount", 2)
        intent.putExtra("player1Name", player1Name)
        intent.putExtra("player2Name", player2Name)
        startActivity(intent)
    }


    enum class Turn {
        NOUGHT,
        CROSS
    }

    private var firstTurn = Turn.CROSS
    private var currentTurn = Turn.CROSS

    private var crossesScore = 0
    private var noughtsScore = 0

    private var boardList = mutableListOf<Button>()

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowTitleEnabled(false)



        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playerCount = intent.getIntExtra("playerCount", 1)

        if (playerCount == 1) {
            // Гра для одного гравця
            resetBoard()
        } else if (playerCount == 2) {
            // Гра для двох гравців
            player1Name = intent.getStringExtra("player1Name") ?: "Гравець 1"
            player2Name = intent.getStringExtra("player2Name") ?: "Гравець 2"
            //  player1Name та player2Name у грі для двох гравців
            currentTurn = if (Random.nextBoolean()) Turn.CROSS else Turn.NOUGHT
            setTurnLabel()
        }


        initBoard()



    }


    private fun initBoard() {
        boardList.add(binding.a1)
        boardList.add(binding.a2)
        boardList.add(binding.a3)
        boardList.add(binding.b1)
        boardList.add(binding.b2)
        boardList.add(binding.b3)
        boardList.add(binding.c1)
        boardList.add(binding.c2)
        boardList.add(binding.c3)
    }


// При кліку на кнопку
fun boardTapped(view: View) {
    if (view !is Button || view.text.isNotEmpty()) {
        // Якщо кнопка не є клітинкою або вже зайнята, виходимо з методу.
        return
    }

    addToBoard(view)



    if (checkForVictory(NOUGHT) || checkForVictory(CROSS)) {
        val winnerName = if (currentTurn == Turn.CROSS) player2Name else player1Name
        if (winnerName == player1Name) {
            crossesScore++
        } else {
            noughtsScore++
        }
        result("$winnerName виграв!")
    } else if (fullBoard()) {
        result("Нічия")
    }


    // Якщо гра для одного гравця і хід ще не завершено, робіть хід комп'ютера.
    if (playerCount == 1 && !isComputerTurn) {
        // Заблокуйте всі кнопки під час ходу комп'ютера.
        for (button in boardList) {
            button.isClickable = false
        }

        isComputerTurn = true
        computerMove()

    }
}



    private fun computerMove() {
        // Заблокуйте можливість кліку під час ходу комп'ютера.
        isComputerTurn = true

        // Отримуємо список доступних клітинок (кнопок) на дошці.
        val availableButtons = boardList.filter { it.text.isEmpty() }

        if (availableButtons.isNotEmpty()) {
            // Якщо є доступні клітинки, вибираємо одну рандомно.
            val randomIndex = (0 until availableButtons.size).random()
            val selectedButton = availableButtons[randomIndex]

            // Вивести інформацію в консоль (логах) про вибір клітинки.
            Log.d("ComputerMove", "Обрано клітинку: ${selectedButton.id}")

            // Поставте задержку перед встановленням тексту.
            Handler().postDelayed({
                // Встановлюємо текст на клітинці відповідно до поточного ходу (currentTurn).
                selectedButton.text = if (currentTurn == Turn.CROSS) CROSS else NOUGHT
                currentTurn = if (currentTurn == Turn.CROSS) Turn.NOUGHT else Turn.CROSS
                // Позначаємо, що хід комп'ютера завершено.
                isComputerTurn = false
                // Розблокуйте всі кнопки після завершення ходу комп'ютера.
                for (button in boardList) {
                    button.isClickable = true
                }

            }, 1000) // Затримка на 1 секунду (1000 мілісекунд) для емуляції ходу комп'ютера.
        }
    }



    // Отримуємо список порожніх клітинок на дошці
    private fun getEmptyCells(): List<Button> {
        val emptyCells = mutableListOf<Button>()

        for (button in boardList) {
            if (button.text.isEmpty()) {
                emptyCells.add(button)
            }
        }

        return emptyCells
    }

    private fun checkForVictory(s: String): Boolean {

        //horizontal victory
        if (match(binding.a1,s) && match(binding.a2,s) && match(binding.a3,s))
            return true
        if (match(binding.b1,s) && match(binding.b2,s) && match(binding.b3,s))
            return true
        if (match(binding.c1,s) && match(binding.c2,s) && match(binding.c3,s))
            return true

        //vertical victory
        if (match(binding.a1,s) && match(binding.b1,s) && match(binding.c1,s))
            return true
        if (match(binding.a2,s) && match(binding.b2,s) && match(binding.c2,s))
            return true
        if (match(binding.a3,s) && match(binding.b3,s) && match(binding.c3,s))
            return true

        //diagonal victory
        if (match(binding.a1,s) && match(binding.b2,s) && match(binding.c3,s))
            return true
        if (match(binding.a3,s) && match(binding.b2,s) && match(binding.c1,s))
            return true


        return false
    }
    private fun match(button: Button, symbol : String): Boolean = button.text == symbol

    private fun result(title: String) {
        val message = "$player1Name: $crossesScore\n\n$player2Name: $noughtsScore"
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Reset") { _, _ -> resetBoard() }
            .setCancelable(false)
            .show()
    }


    private fun resetBoard() {
        for (button in boardList) {
            button.text = ""

        }
        if (firstTurn == Turn.NOUGHT) {
            firstTurn = Turn.CROSS
        } else if (firstTurn == Turn.CROSS) {
            firstTurn = Turn.NOUGHT
        }


        currentTurn = firstTurn
        setTurnLabel()


    }



    private fun fullBoard(): Boolean {
        for(button in boardList){
            if(button.text == "")
                return false
        }
        return true

    }

    private fun addToBoard(button: Button) {
        if (button.text != "") {
            return
        }

        val symbol: String
        val textColor: Int // Колір тексту

        if (currentTurn == Turn.NOUGHT) {
            symbol = NOUGHT
            textColor = Color.RED //  колір тексту для нолика
            currentTurn = Turn.CROSS
        } else {
            symbol = CROSS
            textColor = Color.BLUE //  колір тексту для хрестика
            currentTurn = Turn.NOUGHT
        }

        button.text = symbol
        button.setTextColor(textColor) // Встановіть колір тексту
        setTurnLabel()
    }


    private fun setTurnLabel() {
        var turnText = when (currentTurn) {
            Turn.CROSS -> player1Name
            Turn.NOUGHT -> player2Name
        }

        turnText = "Хід $turnText"
        binding.turnTV.text = turnText
    }



    companion object{
        const val NOUGHT = "0"
        const val CROSS = "X"

    }

}