package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import org.w3c.dom.Text

class Rooms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)

        val rooms = findViewById<ListView>(R.id.roomsList)

        val arr = arrayOf("room1", "room2", "room3")

        rooms.adapter = CustomAdaptor(arr)

        val newRoom = findViewById<Button>(R.id.newRoom)
        newRoom.setOnClickListener{
            val intent = Intent(this, OnlineSetting::class.java)
            startActivity(intent)
        }


    }

    private class CustomAdaptor(data: Array<String>): BaseAdapter() {
        private val data = data


        override fun getCount(): Int {
            return data.size
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItem(position: Int): Any {
            return "String"
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//            val layoutInflater = LayoutInflater.from(parent?.context)
//            val row = layoutInflater.inflate(R.layout.category_main, parent, false)
//            val title = row.findViewById<TextView>(R.id.category_textView)
//            title.text = mCategories[position]
//            row.setOnClickListener {
//                val next = Intent(parent?.context, TopicOverViewActivity::class.java)
//                next.putExtra("category", position)
//
//                parent?.context?.startActivity(next)
//            }
            val row = TextView(parent?.context)
            row.text = data[position]
            return row
        }

    }
}
