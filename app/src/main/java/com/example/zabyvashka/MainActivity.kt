package com.example.zabyvashka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val label = findViewById<TextView>(R.id.main_label)
        val userData: EditText = findViewById(R.id.main_edit)
        val button: Button = findViewById(R.id.main_button)

        button.setOnClickListener {
            var text = userData.text.toString().trim()
            if (text == "ok")
                Toast.makeText(this, "User enter ok", Toast.LENGTH_SHORT).show()
            else
                label.text = text
        }
    }
}