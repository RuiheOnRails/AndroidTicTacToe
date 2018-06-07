package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class OnlineSetting : AppCompatActivity() {
    private val db = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_setting)
        val thisContext = this

        val boardSizeSpinner = findViewById<Spinner>(R.id.boardSizeSpinner)
        val roomNameText = findViewById<TextView>(R.id.roomNameText)
        val createRoomButton = findViewById<Button>(R.id.createRoomButton)
        val options = arrayOf("3x3", "4x4")

        boardSizeSpinner.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)

        var chosen = 0

        boardSizeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chosen = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosen = 0
            }
        }

        createRoomButton.setOnClickListener {
            val re = Regex("[^A-Za-z0-9 ]")
            val roomName = re.replace(roomNameText.text.toString(), "")
            if (roomName.isEmpty()) {
                Toast.makeText(this, "must provide valid room name", Toast.LENGTH_SHORT).show()
            } else {
                val ref = FirebaseDatabase.getInstance().reference
                val roomObj = ref.child("rooms").child(roomName)


                roomObj.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(thisContext, "try again, server error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val data = p0.value
                        if (data != null) {
                            Toast.makeText(thisContext, "name taken, try another one", Toast.LENGTH_SHORT).show()
                        } else {
                            val room = Room(true, chosen+3, FirebaseAuth.getInstance().currentUser!!.uid)
                            db.child("rooms").child(roomName).setValue(room)
                            if (chosen == 0) {
                                db.child("rooms").child(roomName).child("0").setValue(arrayListOf(0,0,0))
                                db.child("rooms").child(roomName).child("1").setValue(arrayListOf(0,0,0))
                                db.child("rooms").child(roomName).child("2").setValue(arrayListOf(0,0,0))
                            }
                            if (chosen == 1) {
                                db.child("rooms").child(roomName).child("0").setValue(arrayListOf(0,0,0,0))
                                db.child("rooms").child(roomName).child("1").setValue(arrayListOf(0,0,0,0))
                                db.child("rooms").child(roomName).child("2").setValue(arrayListOf(0,0,0,0))
                                db.child("rooms").child(roomName).child("3").setValue(arrayListOf(0,0,0,0))
                            }

                            val intent = Intent(thisContext, HostWaiting::class.java)
                            intent.putExtra("roomName", roomName)
                            intent.putExtra("boardSize", chosen+3)
                            startActivity(intent)
                        }
                    }
                })
            }
        }
    }


    class Room(open: Boolean, boardSize: Int, player1: String) {
        public val open = open
        public val boardSize = boardSize
        public val player1 = player1
        public val first = if (Math.random() > 0.5) 1 else 2
    }
}
