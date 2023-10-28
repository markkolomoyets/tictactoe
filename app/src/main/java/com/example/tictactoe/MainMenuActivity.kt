package com.example.tictactoe

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout

import android.widget.RadioGroup


class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    fun startTwoPlayersGame(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Введіть імена гравців")

        val input1 = EditText(this)
        input1.hint = "Ім'я Гравця 1"
        val input2 = EditText(this)
        input2.hint = "Ім'я Гравця 2"

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(input1)
        layout.addView(input2)
        builder.setView(layout)

        builder.setPositiveButton("Почати гру") { dialog, which ->
            val player1Name = input1.text.toString()
            val player2Name = input2.text.toString()

            startTwoPlayersGameWithNames(player1Name, player2Name)
        }

        builder.setNegativeButton("Скасувати") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun startTwoPlayersGameWithNames(player1Name: String, player2Name: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("playerCount", 2)
        intent.putExtra("player1Name", player1Name)
        intent.putExtra("player2Name", player2Name)
        startActivity(intent)
    }


    fun startGame(view: View) {
        val playerCountRadioGroup = findViewById<RadioGroup>(R.id.playerSelectionGroup)
        val selectedRadioButtonId = playerCountRadioGroup.checkedRadioButtonId

        if (selectedRadioButtonId == R.id.onePlayerRadioButton) {
            // Один гравець
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("playerCount", 1)
            startActivity(intent)
        } else if (selectedRadioButtonId == R.id.twoPlayersRadioButton) {
            // Два гравці
            startTwoPlayersGame(view)
        }
    }

}
