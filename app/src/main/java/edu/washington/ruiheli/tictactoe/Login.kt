package edu.washington.ruiheli.tictactoe

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val actionBar = supportActionBar
        actionBar!!.hide()

        var toSignUp = findViewById<Button>(R.id.toSignUpBtn)
        toSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            this.startActivity(intent)
        }

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val email = findViewById<TextView>(R.id.loginEmailEditText)
        val password = findViewById<TextView>(R.id.loginPasswordEditText)
        val toLocalGame = findViewById<Button>(R.id.toOfflineLocal)
        toLocalGame.setOnClickListener {
            val intent = Intent(this, OfflineSetting::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            if (email.text.isEmpty()){
                Toast.makeText(this, "email cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (password.text.isEmpty()) {
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener {task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainMenu::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "sign in failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
