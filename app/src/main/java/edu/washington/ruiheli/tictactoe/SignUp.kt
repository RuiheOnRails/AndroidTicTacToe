package edu.washington.ruiheli.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text


class SignUp : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val signUpButton:Button =  findViewById(R.id.signUpBtn)
        val signUpEmail:TextView = findViewById(R.id.signUpEmail)
        val password:TextView = findViewById(R.id.signUpPW)
        val passwordConf:TextView = findViewById(R.id.signUpPWConf)
        val displayName:TextView = findViewById(R.id.signUpDisplayName)
        signUpButton.setOnClickListener {
            if (signUpEmail.text.isEmpty()) {
                Toast.makeText(this, "email cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (password.text.isEmpty()) {
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (passwordConf.text.isEmpty()) {
                Toast.makeText(this, "password confirmation cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (displayName.text.isEmpty()) {
                Toast.makeText(this, "display name cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!password.text.toString().equals(passwordConf.text.toString())) {
                Toast.makeText(this, "password must match confirmation", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(signUpEmail.text.toString(), password.text.toString()).addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Toast.makeText(this, "Signed up!!!!!!!!!!!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }



        }
    }
}
