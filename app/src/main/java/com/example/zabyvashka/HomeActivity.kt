package com.example.zabyvashka

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Получаем имя пользователя
        val userName = intent.getStringExtra("USER_NAME") ?: "друг"

        val greetingText = findViewById<TextView>(R.id.greeting_text)
        val emptyText = findViewById<TextView>(R.id.empty_state_text)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        greetingText.text = "Привет, $userName!"

        // Логика меню
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    greetingText.text = "Привет, $userName!"
                    emptyText.text = "Здесь будет список твоих объектов."
                    true
                }
                R.id.nav_add -> {
                    Toast.makeText(this, "Экран добавления", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_profile -> {
                    greetingText.text = "Профиль"
                    emptyText.text = "Пользователь: $userName"
                    true
                }
                else -> false
            }
        }
    }
}