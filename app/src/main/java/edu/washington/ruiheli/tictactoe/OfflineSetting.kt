package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class OfflineSetting : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_setting)

        val boardSize = findViewById<Spinner>(R.id.boardSize)
        val options = arrayOf("3x3", "4x4")
        boardSize.adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, options)
        val start = findViewById<Button>(R.id.startGame)
        var chosen = 0

        boardSize.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                chosen = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                chosen = 0
            }
        }

        start.setOnClickListener {
            if (chosen == 0) {
                val intent = Intent(this, OfflineGameBoard::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, OfflineGameBoard4x4::class.java)
                startActivity(intent)
            }
        }

    }
}
