package edu.washington.ruiheli.tictactoe

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class OfflineGameBoard4x4 : AppCompatActivity() {

    var currentPlayer = 1
    var board = arrayOf(arrayOf(0,0,0,0),arrayOf(0,0,0,0),arrayOf(0,0,0,0),arrayOf(0,0,0,0))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_game_board4x4)
        val currentPlayerTextView = findViewById<TextView>(R.id.currentTurn4x4TextView)
        currentPlayerTextView.text = "X"

        var boardBtns = arrayOf(
                arrayOf(findViewById<Button>(R.id.offline4x4Tile1),
                        findViewById<Button>(R.id.offline4x4Tile2),
                        findViewById<Button>(R.id.offline4x4Tile3),
                        findViewById<Button>(R.id.offline4x4Tile4)),
                arrayOf(findViewById<Button>(R.id.offline4x4Tile5),
                        findViewById<Button>(R.id.offline4x4Tile6),
                        findViewById<Button>(R.id.offline4x4Tile7),
                        findViewById<Button>(R.id.offline4x4Tile8)),
                arrayOf(findViewById<Button>(R.id.offline4x4Tile9),
                        findViewById<Button>(R.id.offline4x4Tile10),
                        findViewById<Button>(R.id.offline4x4Tile11),
                        findViewById<Button>(R.id.offline4x4Tile12)),
                arrayOf(findViewById<Button>(R.id.offline4x4Tile13),
                        findViewById<Button>(R.id.offline4x4Tile14),
                        findViewById<Button>(R.id.offline4x4Tile15),
                        findViewById<Button>(R.id.offline4x4Tile16)))

        for( i in 0 until boardBtns.size){
            for (j in 0 until boardBtns.size ){
                boardBtns[i][j].setOnClickListener {
                    Log.i("BOARD", "clicked on $i, $j")
                    boardBtns[i][j].text = if (currentPlayer == 1) {
                        "X"
                    }else{
                        "O"
                    }
                    currentPlayerTextView.text = if (currentPlayer == 1) {
                        "O"
                    }else{
                        "X"
                    }
                    moveMade(i,j)
                }
            }
        }
    }

    private fun moveMade(row: Int, col: Int) {
        board[row][col] = currentPlayer
        var endGame = false
        var msg = ""
        if (GameUtil.checkWinner(board)) {
            endGame = true
            msg = if (currentPlayer == 1) {
                "X "
            } else {
                "O "
            }

            msg += "Won !"

        } else if (GameUtil.checkTie(board)) {
            msg = "Game Tied!"
            endGame = true
        }


        if (endGame) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle(msg)
            builder.setMessage("Do you want to play another game?")
            builder.setNegativeButton("No", { dialog: DialogInterface?, which: Int ->
                var auth = FirebaseAuth.getInstance()
                if (auth.currentUser == null) {
                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, MainMenu::class.java)
                    startActivity(intent)
                }
            })
            builder.setPositiveButton("Yes", { dialog: DialogInterface?, which: Int ->
                var intent = Intent(this, OfflineGameBoard4x4::class.java)
                startActivity(intent)
            })
            builder.show()
        }

        currentPlayer = if (currentPlayer == 1) {
            -1
        } else {
            1
        }
    }
}
