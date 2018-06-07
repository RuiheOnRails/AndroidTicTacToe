package edu.washington.ruiheli.tictactoe

import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.support.v7.app.AlertDialog
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OnlineGameBoard : AppCompatActivity() {
    var currentPlayer = 1
    var board = arrayOf(arrayOf(0,0,0),arrayOf(0,0,0),arrayOf(0,0,0))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_game_board)

        val thiscontext = this

        val playerSymbol = findViewById<TextView>(R.id.playerTagTextView)
        val currentPlayerTextView = findViewById<TextView>(R.id.currentTurnTextViewOnline)
        currentPlayerTextView.text = "X"
        val dbref = FirebaseDatabase.getInstance().reference
        val room = dbref.child("rooms").child(intent.getStringExtra("roomName"))

        var boardBtns = arrayOf(
                arrayOf(findViewById<Button>(R.id.online3x3Tile1),
                        findViewById<Button>(R.id.online3x3Tile2),
                        findViewById<Button>(R.id.online3x3Tile3)),
                arrayOf(findViewById<Button>(R.id.online3x3Tile4),
                        findViewById<Button>(R.id.online3x3Tile5),
                        findViewById<Button>(R.id.online3x3Tile6)),
                arrayOf(findViewById<Button>(R.id.online3x3Tile7),
                        findViewById<Button>(R.id.online3x3Tile8),
                        findViewById<Button>(R.id.online3x3Tile9)))

        for( i in 0 until boardBtns.size){
            for (j in 0 until boardBtns.size ){
                boardBtns[i][j].setOnClickListener {
                    if (currentPlayerTextView.text.toString() != playerSymbol.text.toString()) {
                        Toast.makeText(this, "It is not your turn yet", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.i("BOARD", "clicked on $i, $j")
                        boardBtns[i][j].isEnabled = false
                        boardBtns[i][j].text = if (currentPlayer == 1) {
                            "X"
                        }else{
                            "O"
                        }
                        if (currentPlayer == 1) {
                            room.child(i.toString()).child(j.toString()).setValue(-1)
                        } else {
                            room.child(i.toString()).child(j.toString()).setValue(1)
                        }

                        currentPlayerTextView.text = if (currentPlayer == 1) {
                            "O"
                        }else{
                            "X"
                        }
                        board[i][j] = currentPlayer

                    }
                }
            }
        }



        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("forfeit").getValue(String::class.java) != null) {
                    FirebaseDatabase.getInstance().reference.child("rooms").child(intent.getStringExtra("roomName")).removeValue()
                    val intent = Intent(thiscontext, MainMenu::class.java)
                    Toast.makeText(thiscontext, "Game ended due to disconnection", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                } else {
                    if (dataSnapshot.child("player1").getValue(String::class.java)!=null && dataSnapshot.child("player1").getValue(String::class.java) == FirebaseAuth.getInstance().currentUser!!.uid) {
                        playerSymbol.text = "X"
                    } else if (dataSnapshot.child("player1").getValue(String::class.java) != null) {
                        playerSymbol.text = "O"
                    }

                    currentPlayerTextView.text = if (currentPlayer == 1) {
                        "O"
                    }else{
                        "X"
                    }

                    var moved = false

                    for (i in 0 until boardBtns.size){
                        for(j in 0 until boardBtns.size) {
                            val value = dataSnapshot.child(i.toString()).child(j.toString()).getValue(Int::class.java)
                            if (value == -1) {
                                boardBtns[i][j].text = "X"
                                board[i][j] = -1
                                boardBtns[i][j].isEnabled = false
                                moved = true
                            } else if (value == 1) {
                                boardBtns[i][j].text = "O"
                                board[i][j] = 1
                                boardBtns[i][j].isEnabled = false
                                moved = true
                            }
                        }
                    }
                    if (dataSnapshot.child("player1").getValue(String::class.java) != null) {
                        moveMade()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        room.addValueEventListener(eventListener)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().reference.child("rooms").child(intent.getStringExtra("roomName")).child("forfeit").setValue(FirebaseAuth.getInstance().currentUser!!.uid)
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val gamesTied = p0.child("gamesLost").getValue(Int::class.java)
                if (gamesTied != null) {
                    FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesLost").setValue(gamesTied+1)
                }
                val gamesPlayed = p0.child("gamesPlayed").getValue(Int::class.java)
                if (gamesPlayed != null) {
                    FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesPlayed").setValue(gamesPlayed+1)
                }
            }
        })

        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }


    private fun moveMade(){
        var endGame = false
        var msg = ""
        if(GameUtil.checkWinner(board)){
            endGame = true
            msg = if(currentPlayer == 1) {
                "X "
            }else {
                "O "
            }

            msg += "Won!"


            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (msg=="X Won!" && findViewById<TextView>(R.id.playerTagTextView).text.toString()=="X") {
                        val gamesWon = p0.child("gamesWon").getValue(Int::class.java)
                        if (gamesWon != null) {
                            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesWon").setValue(gamesWon+1)
                        }
                    } else if (msg=="O Won!" && findViewById<TextView>(R.id.playerTagTextView).text.toString()=="O"){
                        val gamesWon = p0.child("gamesWon").getValue(Int::class.java)
                        if (gamesWon != null) {
                            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesWon").setValue(gamesWon+1)
                        }
                    } else {
                        val gamesLost = p0.child("gamesLost").getValue(Int::class.java)
                        if (gamesLost != null) {
                            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesLost").setValue(gamesLost+1)
                        }
                    }
                    val gamesPlayed = p0.child("gamesPlayed").getValue(Int::class.java)
                    if (gamesPlayed != null) {
                        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesPlayed").setValue(gamesPlayed+1)
                    }
                }
            })

        }else if(GameUtil.checkTie(board)){
            msg = "Game Tied!"
            endGame = true
            FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val gamesTied = p0.child("gamesTied").getValue(Int::class.java)
                    if (gamesTied != null) {
                        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesTied").setValue(gamesTied+1)
                    }
                    val gamesPlayed = p0.child("gamesPlayed").getValue(Int::class.java)
                    if (gamesPlayed != null) {
                        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child("gamesPlayed").setValue(gamesPlayed+1)
                    }
                }
            })
        }


        if (endGame){
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().reference.child("rooms").child(intent.getStringExtra("roomName")).removeValue()
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)

        }

        currentPlayer = if(currentPlayer == 1 ){
            -1
        }else{
            1
        }
    }
}
