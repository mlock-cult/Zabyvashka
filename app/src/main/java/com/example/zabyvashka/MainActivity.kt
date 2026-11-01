package com.example.zabyvashka

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailInput = findViewById<EditText>(R.id.main_edit_email)
        val passwordInput = findViewById<EditText>(R.id.main_edit_password)
        val loginButton = findViewById<Button>(R.id.main_button)
        val registerLink = findViewById<TextView>(R.id.register_link)
        val eyeIcon = findViewById<ImageView>(R.id.eye_icon) // Находим глазик

        // Переменная для отслеживания: скрыт пароль или нет
        var isPasswordVisible = false

        // Логика для глазика
        eyeIcon.setOnClickListener {
            if (isPasswordVisible) {
                // Скрываем пароль (превращаем в точки)
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                isPasswordVisible = false
            } else {
                // Показываем пароль (буквами)
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                isPasswordVisible = true
            }
            // Ставим курсор в конец текста, чтобы он не прыгал в начало
            passwordInput.setSelection(passwordInput.text.length)
        }

        // Обработка кнопки "Войти"
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Вы вошли как: $email", Toast.LENGTH_LONG).show()
            }
        }

        // Обработка ссылки
        registerLink.setOnClickListener {
            Toast.makeText(this, "Переход на экран регистрации", Toast.LENGTH_SHORT).show()
        }
    }
}