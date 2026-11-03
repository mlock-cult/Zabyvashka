package com.example.zabyvashka

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

        val emailTitle = findViewById<TextView>(R.id.email_title)
        val passwordTitle = findViewById<TextView>(R.id.password_title)

        // Контейнеры (капсулы) и иконки внутри них
        val emailContainer = findViewById<LinearLayout>(R.id.email_container)
        val passContainer = findViewById<LinearLayout>(R.id.password_container)

        val emailIcon = emailContainer.getChildAt(0) as ImageView
        val lockIcon = passContainer.getChildAt(0) as ImageView

        // Получаем email из регистрации (автозаполнение)
        val registeredEmail = intent.getStringExtra("USER_EMAIL")
        if (!registeredEmail.isNullOrEmpty()) {
            emailInput.setText(registeredEmail)
            passwordInput.requestFocus()
        }

        // 2. Логика для "глазика"
        var isPasswordVisible = false
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            if (isPasswordVisible) {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye2)
            } else {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye1)
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        // 3. Кнопка "Войти"
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            // Цвета для темной темы
            val defaultTextColor = Color.parseColor("#FFFFFF")
            val defaultDimColor = Color.parseColor("#80FFFFFF")
            val errorTitleColor = Color.parseColor("#FF5252")

            // СБРОС ОШИБОК
            emailTitle.text = "Email"
            emailTitle.setTextColor(defaultTextColor)
            passwordTitle.text = "Пароль"
            passwordTitle.setTextColor(defaultTextColor)

            emailContainer.setBackgroundResource(R.drawable.bg_input_field)
            passContainer.setBackgroundResource(R.drawable.bg_input_field)

            emailInput.setHintTextColor(defaultDimColor)
            passwordInput.setHintTextColor(defaultDimColor)

            emailIcon.setColorFilter(defaultDimColor)
            eyeIcon.setColorFilter(defaultDimColor)
            lockIcon.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

            var hasError = false

            // ОШИБКА EMAIL
            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                emailTitle.text = "Введите корректный email"
                emailTitle.setTextColor(errorTitleColor)

                emailContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                emailInput.setHintTextColor(errorTitleColor)
                emailIcon.setColorFilter(errorTitleColor)
                hasError = true
            }

            // ОШИБКА ПАРОЛЯ
            if (password.isEmpty() || password.length < 6) {
                passwordTitle.text = "Пароль от 6 символов"
                passwordTitle.setTextColor(errorTitleColor)

                passContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                passwordInput.setHintTextColor(errorTitleColor)
                eyeIcon.setColorFilter(errorTitleColor)
                lockIcon.setColorFilter(errorTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            // ЕСЛИ ОШИБОК НЕТ — ПРОВЕРЯЕМ В БАЗЕ
            if (!hasError) {
                val db = AppDatabase.getDatabase(this)
                val userDao = db.userDao()

                CoroutineScope(Dispatchers.Main).launch {
                    val user = userDao.getUserByEmail(email)

                    if (user != null && PasswordHasher.verify(password, user.password)) {
                        // Успешный вход
                        Toast.makeText(this@MainActivity, "Вы вошли как: ${user.name}", Toast.LENGTH_LONG).show()
                        // Переход на главный экран
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        intent.putExtra("USER_NAME", user.name)
                        startActivity(intent)
                        finish()
                    } else {
                        // ОШИБКА БАЗЫ ДАННЫХ
                        emailTitle.text = "Неверный email или пароль"
                        emailTitle.setTextColor(errorTitleColor)
                        passwordTitle.text = "Неверный email или пароль"
                        passwordTitle.setTextColor(errorTitleColor)

                        emailContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                        passContainer.setBackgroundResource(R.drawable.bg_input_field_error)

                        emailInput.setHintTextColor(errorTitleColor)
                        passwordInput.setHintTextColor(errorTitleColor)

                        eyeIcon.setColorFilter(errorTitleColor)
                        lockIcon.setColorFilter(errorTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
                        emailIcon.setColorFilter(errorTitleColor)
                    }
                }
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