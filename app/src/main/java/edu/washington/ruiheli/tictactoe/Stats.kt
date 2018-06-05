package edu.washington.ruiheli.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.FirebaseDatabase



class Stats : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        var auth = FirebaseAuth.getInstance()
        val rootRef = FirebaseDatabase.getInstance().reference
        var id = auth.currentUser!!.uid   // current user id
        val userRef = rootRef.child("users").child(id)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(ds: DataSnapshot) {
                val lost = ds.child("gamesLost").getValue(Int::class.java)
                Log.d("TAG", lost.toString())
                val play = ds.child("gamesPlayed").getValue(Int::class.java)
                Log.d("TAG", play.toString())
                val tie = ds.child("gamesTied").getValue(Int::class.java)
                Log.d("TAG", tie.toString())
                val won = ds.child("gamesWon").getValue(Int::class.java)
                Log.d("TAG", won.toString())
                var ratio = findViewById<TextView>(R.id.stat)
                ratio.text = "$won vs. $lost"
                var lostBtn = findViewById<TextView>(R.id.lost)
                var playBtn = findViewById<TextView>(R.id.played)
                var tieBtn = findViewById<TextView>(R.id.tied)
                var wonBtn = findViewById<TextView>(R.id.won)
                lostBtn.append(lost.toString())
                playBtn.append(play.toString())
                tieBtn.append(tie.toString())
                wonBtn.append(won.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })
    }
}
