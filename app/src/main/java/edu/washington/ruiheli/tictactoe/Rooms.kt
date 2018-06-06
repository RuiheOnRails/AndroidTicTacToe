package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.R.menu
import android.view.Menu


class Rooms : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)
        val thisContext = this

        val rooms = findViewById<ListView>(R.id.roomsList)

        val ref = FirebaseDatabase.getInstance().reference
        val roomsRef = ref.child("rooms")

        val actionBar = supportActionBar


        roomsRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(thisContext, "try again, server error", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.value != null) {
                    val arrOfNames = arrayOfNulls<String>(p0.childrenCount.toInt())
                    var idx = 0
                    p0.children.forEach {
                        val tempOpen =  it.child("open").getValue(Boolean::class.java)

                        val tempSize = it.child("boardSize").getValue(Int::class.java)


                        if (it.key!=null) {
                            arrOfNames[idx] = it.key.toString() + " | " + tempSize + "x" + tempSize + " | " + if(tempOpen!=null && tempOpen) "open" else "closed"
                        }
                        idx++
                    }
                    rooms.adapter = CustomAdaptor(arrOfNames)
                } else {
                    rooms.adapter = null
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.create-> {
            val intent = Intent(this, OnlineSetting::class.java)
            startActivity(intent)
            // User chose the "Settings" item, show the app settings UI...
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private class CustomAdaptor(data: Array<String?>): BaseAdapter() {
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
            row.textSize = 30f
            return row
        }

    }
}
