package com.example.zabyvashka

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Находим элементы
        val emailInput = findViewById<EditText>(R.id.main_edit_email)
        val passwordInput = findViewById<EditText>(R.id.main_edit_password)
        val loginButton = findViewById<Button>(R.id.main_button)
        val registerLink = findViewById<TextView>(R.id.register_link)
        val eyeIcon = findViewById<ImageView>(R.id.eye_icon)
        val footerGithubLink = findViewById<TextView>(R.id.footer_github_link)

        // Находим заголовки один раз (выносим наверх для удобства)
        val emailTitle = findViewById<TextView>(R.id.email_title)
        val passwordTitle = findViewById<TextView>(R.id.password_title)

        // 2. Логика для "глазика"
        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                // Показываем пароль
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                // Меняем иконку на перечеркнутый глаз (eye2)
                eyeIcon.setImageResource(R.drawable.eye2)
            } else {
                // Скрываем пароль
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                // Возвращаем обычный глаз (eye1)
                eyeIcon.setImageResource(R.drawable.eye1)
            }

            // Ставим курсор в конец, чтобы он не прыгал в начало
            passwordInput.setSelection(passwordInput.text.length)
        }

        // 3. Кнопка "Войти"
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            val defaultTextColor = android.graphics.Color.parseColor("#594139")
            val defaultDimColor = android.graphics.Color.parseColor("#99B6BECE")
            val defaultIconColor = android.graphics.Color.parseColor("#996B7C93")

            val errorTitleColor = android.graphics.Color.parseColor("#D32F2F")
            val errorDimColor = android.graphics.Color.parseColor("#80E57373")

            // Находим контейнер пароля и иконку замка
            val passwordContainer = findViewById<android.widget.LinearLayout>(R.id.password_container)
            val lockIcon = passwordContainer.getChildAt(0) as ImageView

            // СБРОС
            emailTitle.text = "Email"
            emailTitle.setTextColor(defaultTextColor)

            passwordTitle.text = "Пароль"
            passwordTitle.setTextColor(defaultTextColor)

            emailInput.setHintTextColor(defaultDimColor)
            passwordInput.setHintTextColor(defaultDimColor)

            eyeIcon.setColorFilter(defaultIconColor)
            // Возвращаем замку тусклый цвет через SRC_IN
            lockIcon.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
            emailInput.compoundDrawables[0]?.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

            var hasError = false

            // ОШИБКА EMAIL
            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                emailTitle.text = "Введите корректный email"
                emailTitle.setTextColor(errorTitleColor)

                emailInput.setHintTextColor(errorDimColor)
                emailInput.compoundDrawables[0]?.setColorFilter(errorDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

                hasError = true
            }

            // ОШИБКА ПАРОЛЯ
            if (password.isEmpty() || password.length < 6) {
                passwordTitle.text = "Пароль от 6 символов"
                passwordTitle.setTextColor(errorTitleColor)

                passwordInput.setHintTextColor(errorDimColor)
                eyeIcon.setColorFilter(errorDimColor)
                // Красим замок в красный через SRC_IN
                lockIcon.setColorFilter(errorDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

                hasError = true
            }

            if (!hasError) {
                Toast.makeText(this, "Вы вошли как: $email", Toast.LENGTH_LONG).show()
            }
        }

        // 4. Ссылка "Зарегистрироваться"
        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // 5. Ссылка на GitHub
        footerGithubLink.setOnClickListener {
            val url = "https://github.com/mlock-cult/Zabyvashka"
            try {
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Не удалось открыть браузер", Toast.LENGTH_SHORT).show()
            }
        }
    }
}