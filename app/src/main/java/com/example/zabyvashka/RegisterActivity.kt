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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Находим элементы ввода
        val nameInput = findViewById<EditText>(R.id.reg_edit_name)
        val emailInput = findViewById<EditText>(R.id.reg_edit_email)
        val passwordInput = findViewById<EditText>(R.id.reg_edit_password)
        val password2Input = findViewById<EditText>(R.id.reg_edit_password2)
        val registerButton = findViewById<Button>(R.id.reg_button)
        val loginLink = findViewById<TextView>(R.id.login_link)

        // Заголовки
        val nameTitle = findViewById<TextView>(R.id.reg_name_title)
        val emailTitle = findViewById<TextView>(R.id.reg_email_title)
        val passwordTitle = findViewById<TextView>(R.id.reg_password_title)
        val password2Title = findViewById<TextView>(R.id.reg_password2_title)

        // Глазики
        val eyeIcon = findViewById<ImageView>(R.id.reg_eye_icon)
        val eyeIcon2 = findViewById<ImageView>(R.id.reg_eye_icon2)

        // Контейнеры (капсулы) и иконки внутри них
        val nameContainer = findViewById<LinearLayout>(R.id.reg_name_container)
        val emailContainer = findViewById<LinearLayout>(R.id.reg_email_container)
        val passwordContainer = findViewById<LinearLayout>(R.id.reg_password_container)
        val password2Container = findViewById<LinearLayout>(R.id.reg_password2_container)

        val nameIcon = nameContainer.getChildAt(0) as ImageView
        val emailIcon = emailContainer.getChildAt(0) as ImageView
        val lockIcon = passwordContainer.getChildAt(0) as ImageView
        val lockIcon2 = password2Container.getChildAt(0) as ImageView

        // Цвета для темной темы
        val defaultTextColor = Color.parseColor("#FFFFFF")
        val defaultDimColor = Color.parseColor("#80FFFFFF")
        val errorTitleColor = Color.parseColor("#FF5252")

        // --- ГЛАЗИКИ (РАБОТАЮТ СИНХРОННО) ---
        var isPasswordVisible = false

        fun togglePasswords() {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                password2Input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye2)
                eyeIcon2.setImageResource(R.drawable.eye2)
            } else {
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                password2Input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                eyeIcon.setImageResource(R.drawable.eye1)
                eyeIcon2.setImageResource(R.drawable.eye1)
            }
            passwordInput.setSelection(passwordInput.text.length)
            password2Input.setSelection(password2Input.text.length)
        }

        eyeIcon.setOnClickListener { togglePasswords() }
        eyeIcon2.setOnClickListener { togglePasswords() }

        // --- КНОПКА «ЗАРЕГИСТРИРОВАТЬСЯ» ---
        registerButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val password2 = password2Input.text.toString().trim()

            // СБРОС ОШИБОК
            nameTitle.text = "Имя"; nameTitle.setTextColor(defaultTextColor)
            emailTitle.text = "Email"; emailTitle.setTextColor(defaultTextColor)
            passwordTitle.text = "Пароль"; passwordTitle.setTextColor(defaultTextColor)
            password2Title.text = "Повторите пароль"; password2Title.setTextColor(defaultTextColor)

            // Сброс фонов (возвращаем стекло)
            nameContainer.setBackgroundResource(R.drawable.bg_input_field)
            emailContainer.setBackgroundResource(R.drawable.bg_input_field)
            passwordContainer.setBackgroundResource(R.drawable.bg_input_field)
            password2Container.setBackgroundResource(R.drawable.bg_input_field)

            // Сброс иконок и подсказок
            nameInput.setHintTextColor(defaultDimColor)
            emailInput.setHintTextColor(defaultDimColor)
            passwordInput.setHintTextColor(defaultDimColor)
            password2Input.setHintTextColor(defaultDimColor)

            nameIcon.setColorFilter(defaultDimColor)
            emailIcon.setColorFilter(defaultDimColor)
            eyeIcon.setColorFilter(defaultDimColor)
            eyeIcon2.setColorFilter(defaultDimColor)
            lockIcon.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
            lockIcon2.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

            var hasError = false

            // ОШИБКА ИМЕНИ
            if (name.isEmpty()) {
                nameTitle.text = "Введите имя"
                nameTitle.setTextColor(errorTitleColor)
                nameContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                nameInput.setHintTextColor(errorTitleColor)
                nameIcon.setColorFilter(errorTitleColor)
                hasError = true
            }

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
                passwordContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                passwordInput.setHintTextColor(errorTitleColor)
                eyeIcon.setColorFilter(errorTitleColor)
                lockIcon.setColorFilter(errorTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            // ОШИБКА ПОВТОРА ПАРОЛЯ
            if (password2.isEmpty() || password2 != password) {
                password2Title.text = "Пароли не совпадают"
                password2Title.setTextColor(errorTitleColor)
                password2Container.setBackgroundResource(R.drawable.bg_input_field_error)
                password2Input.setHintTextColor(errorTitleColor)
                eyeIcon2.setColorFilter(errorTitleColor)
                lockIcon2.setColorFilter(errorTitleColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            // РАБОТА С БАЗОЙ
            if (!hasError) {
                val db = AppDatabase.getDatabase(this)
                val userDao = db.userDao()

                CoroutineScope(Dispatchers.Main).launch {
                    val existingUser = userDao.getUserByEmail(email)

                    if (existingUser != null) {
                        Toast.makeText(this@RegisterActivity, "Этот email уже зарегистрирован", Toast.LENGTH_LONG).show()
                        // Красим Email в красный, так как он занят
                        emailTitle.text = "Email уже занят"
                        emailTitle.setTextColor(errorTitleColor)
                        emailContainer.setBackgroundResource(R.drawable.bg_input_field_error)
                        emailInput.setHintTextColor(errorTitleColor)
                        emailIcon.setColorFilter(errorTitleColor)
                    } else {
                        // УСПЕХ
                        val hashedPassword = PasswordHasher.hash(password)
                        val newUser = User(name = name, email = email, password = hashedPassword)
                        userDao.insertUser(newUser)

                        Toast.makeText(this@RegisterActivity, "Аккаунт создан: $name", Toast.LENGTH_LONG).show()

                        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                        intent.putExtra("USER_EMAIL", email)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        // Ссылка «Войти» — назад на логин
        loginLink.setOnClickListener {
            finish()
        }
    }
}