package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var toSignUp = findViewById<Button>(R.id.toSignUpBtn)
        toSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            this.startActivity(intent)
        }
    }
}
