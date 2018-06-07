package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener



class HostWaiting : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_waiting)
        val thiscontext = this


        var host = findViewById<TextView>(R.id.host)
        var auth = FirebaseAuth.getInstance()
        val rootRef = FirebaseDatabase.getInstance().reference
        var user = auth.currentUser
        host.append(user!!.displayName)
        val roomName = intent.getStringExtra("roomName")
        var room = rootRef.child("rooms").child(roomName)

        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("player2").getValue(String::class.java) != null) {
                    if (intent.getIntExtra("boardSize", 3) == 3) {
                        val intent = Intent(thiscontext, OnlineGameBoard::class.java)
                        intent.putExtra("roomName", roomName)
                        room.removeEventListener(this)
                        startActivity(intent)
                    } else {
                        val intent = Intent(thiscontext, OnlineGameBoard4x4::class.java)
                        intent.putExtra("roomName", roomName)
                        intent.putExtra("host", FirebaseAuth.getInstance().currentUser!!.displayName)
                        room.removeEventListener(this)
                        startActivity(intent)
                    }

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        room.addValueEventListener(eventListener)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        FirebaseDatabase.getInstance().reference.child("rooms").child(intent.getStringExtra("roomName")).removeValue()

    }

}
