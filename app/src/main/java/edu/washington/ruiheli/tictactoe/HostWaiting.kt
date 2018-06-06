package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

        var host = findViewById<TextView>(R.id.host)
        var auth = FirebaseAuth.getInstance()
        val rootRef = FirebaseDatabase.getInstance().reference
        var user = auth.currentUser
        host.append(user!!.displayName)
        var rooms = rootRef.child("rooms")
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    if (ds.child("player1").getValue(String::class.java) == FirebaseAuth.getInstance().currentUser!!.uid) {
                        if (!ds.child("open").getValue(Boolean::class.java)!!) {
                            val intent = Intent(this@HostWaiting, OnlineGameBoard::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        rooms.addListenerForSingleValueEvent(eventListener)

    }
}
