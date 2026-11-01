package com.example.zabyvashka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Теперь используем обычный EditText
        val emailInput = findViewById<EditText>(R.id.main_edit_email)
        val passwordInput = findViewById<EditText>(R.id.main_edit_password)
        val loginButton = findViewById<Button>(R.id.main_button)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Вы вошли как: $email", Toast.LENGTH_LONG).show()
            }
        }
    }
}