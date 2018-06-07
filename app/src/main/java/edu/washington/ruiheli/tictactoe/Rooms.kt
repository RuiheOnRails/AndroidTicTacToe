package edu.washington.ruiheli.tictactoe

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.auth.FirebaseAuth


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
                    val arrOfSizes = IntArray(p0.childrenCount.toInt())
                    val arrOfOpen = BooleanArray(p0.childrenCount.toInt())
                    val arrOfRoomKey = arrayOfNulls<String>(p0.childrenCount.toInt())
                    var idx = 0
                    val allHost = arrayOfNulls<String>(p0.childrenCount.toInt())
                    p0.children.forEach {
                        val tempOpen =  it.child("open").getValue(Boolean::class.java)

                        val host = it.child("player1Name").getValue(String::class.java)
                        if (host != null) {
                            allHost[idx] = host
                        }
                        val tempSize = it.child("boardSize").getValue(Int::class.java)
                        if (tempSize != null) {
                            arrOfSizes[idx] = tempSize
                        }
                        if (tempOpen != null) {
                            arrOfOpen[idx] = tempOpen
                        }

                        if (it.key!=null) {
                            arrOfRoomKey[idx] = it.key.toString()
                            arrOfNames[idx] = it.key.toString() + " | " + tempSize + "x" + tempSize + " | " + if(tempOpen!=null && tempOpen) "open" else "closed"
                        }
                        idx++
                    }
                    rooms.adapter = CustomAdaptor(thisContext, arrOfNames, arrOfSizes, arrOfOpen, arrOfRoomKey, allHost)
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

    private class CustomAdaptor(context: Context, data: Array<String?>, sizes: IntArray, opens: BooleanArray,
                                keys: Array<String?>, host: Array<String?>): BaseAdapter() {
        private val data = data
        private val opens = opens
        private val sizes = sizes
        private val keys = keys
        private val myContext =context
        private val host = host

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
            val rowLayoutInflater = LayoutInflater.from(myContext)
            val rowLayout = rowLayoutInflater.inflate(R.layout.row_layout, parent, false)
            val room = rowLayout.findViewById<TextView>(R.id.roomName)
            val roomSize = rowLayout.findViewById<TextView>(R.id.roomSize)
            val roomStatus = rowLayout.findViewById<TextView>(R.id.roomStatus)
            val curhost = rowLayout.findViewById<TextView>(R.id.roomHost)
            curhost.text= host[position]

            room.text = keys[position].toString()
            roomSize.text = sizes[position].toString() + " X  " + sizes[position].toString()
            if(opens[position]) {
                roomStatus.text = "OPEN"
            } else {
                roomStatus.text = "CLOSE"
            }
            //val row = TextView(parent?.context)
            //row.text = data[position]
            Log.i("Rooms", data[position])
            // row.textSize = 30f
            rowLayout.setOnClickListener {
                if (!opens[position]) {
                    Toast.makeText(parent?.context, "room is not open", Toast.LENGTH_SHORT).show()
                } else {
                    val ref = FirebaseDatabase.getInstance().reference

                    ref.child("rooms").child(keys[position].toString()).child("open").setValue(false)
                    ref.child("rooms").child(keys[position].toString()).child("player2").setValue(FirebaseAuth.getInstance().currentUser!!.uid)

                    ref.child("rooms").child(keys[position].toString()).child("player2Name")
                            .setValue(FirebaseAuth.getInstance().currentUser!!.displayName!!)

                    if (sizes[position] == 3) {
                        val intent = Intent(parent?.context, OnlineGameBoard::class.java)
                        intent.putExtra("roomName", keys[position])
                        parent?.context?.startActivity(intent)
                    } else{
                        val intent = Intent(parent?.context, OnlineGameBoard4x4::class.java)
                        intent.putExtra("roomName", keys[position])
                        parent?.context?.startActivity(intent)
                    }
                }

            }
            return rowLayout
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainMenu::class.java)
        startActivity(intent)
    }
}
