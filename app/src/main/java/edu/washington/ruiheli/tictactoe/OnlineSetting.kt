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
            if (roomNameText.text.isEmpty()) {
                Toast.makeText(this, "must provide room name", Toast.LENGTH_SHORT).show()
            } else {
                val ref = FirebaseDatabase.getInstance().reference
                val roomObj = ref.child("rooms").child(roomNameText.text.toString())

                roomObj.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(thisContext, "try again, server error", Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        val data = p0.value
                        if (data != null) {
                            Toast.makeText(thisContext, "name taken, try another one", Toast.LENGTH_SHORT).show()
                        } else {
                            val room = Room(true, chosen+3)
                            db.child("rooms").child(roomNameText.text.toString()).setValue(room)
                            val intent = Intent(thisContext, HostWaiting::class.java)
                            startActivity(intent)
                        }
                    }
                })
            }
        }
    }


    class Room(open: Boolean, boardSize: Int) {
        public val open = open
        public val boardSize = boardSize
    }
}
