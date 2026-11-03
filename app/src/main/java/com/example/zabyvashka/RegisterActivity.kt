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
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailInput = findViewById<EditText>(R.id.reg_edit_email)
        val passwordInput = findViewById<EditText>(R.id.reg_edit_password)
        val password2Input = findViewById<EditText>(R.id.reg_edit_password2)
        val registerButton = findViewById<Button>(R.id.reg_button)
        val loginLink = findViewById<TextView>(R.id.login_link)

        val emailTitle = findViewById<TextView>(R.id.reg_email_title)
        val passwordTitle = findViewById<TextView>(R.id.reg_password_title)
        val password2Title = findViewById<TextView>(R.id.reg_password2_title)

        val eyeIcon = findViewById<ImageView>(R.id.reg_eye_icon)
        val eyeIcon2 = findViewById<ImageView>(R.id.reg_eye_icon2)

        val passwordContainer = findViewById<LinearLayout>(R.id.reg_password_container)
        val lockIcon = passwordContainer.getChildAt(0) as ImageView
        val password2Container = findViewById<LinearLayout>(R.id.reg_password2_container)
        val lockIcon2 = password2Container.getChildAt(0) as ImageView

        val nameInput = findViewById<EditText>(R.id.reg_edit_name)
        val nameTitle = findViewById<TextView>(R.id.reg_name_title)

        // Цвета
        val defaultTextColor = Color.parseColor("#594139")
        val defaultDimColor = Color.parseColor("#99B6BECE")
        val defaultIconColor = Color.parseColor("#996B7C93")
        val errorTitleColor = Color.parseColor("#D32F2F")
        val errorDimColor = Color.parseColor("#80E57373")

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
            // Курсоры в конец
            passwordInput.setSelection(passwordInput.text.length)
            password2Input.setSelection(password2Input.text.length)
        }
        // Клик по первому глазику
        eyeIcon.setOnClickListener { togglePasswords() }
        // Клик по второму глазику
        eyeIcon2.setOnClickListener { togglePasswords() }

        // Кнопка «Зарегистрироваться»
        registerButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val password2 = password2Input.text.toString().trim()

            // СБРОС
            emailTitle.text = "Email"; emailTitle.setTextColor(defaultTextColor)
            passwordTitle.text = "Пароль"; passwordTitle.setTextColor(defaultTextColor)
            password2Title.text = "Повторите пароль"; password2Title.setTextColor(defaultTextColor)

            emailInput.setHintTextColor(defaultDimColor)
            passwordInput.setHintTextColor(defaultDimColor)
            password2Input.setHintTextColor(defaultDimColor)

            nameTitle.text = "Имя"
            nameTitle.setTextColor(defaultTextColor)
            nameInput.setHintTextColor(defaultDimColor)

            eyeIcon.setColorFilter(defaultIconColor)
            eyeIcon2.setColorFilter(defaultIconColor)
            lockIcon.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
            lockIcon2.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
            emailInput.compoundDrawables[0]?.setColorFilter(defaultDimColor, android.graphics.PorterDuff.Mode.SRC_IN)

            var hasError = false

            // Имя
            if (nameInput.text.toString().trim().isEmpty()) {
                nameTitle.text = "Введите имя"
                nameTitle.setTextColor(errorTitleColor)
                nameInput.setHintTextColor(errorDimColor)
                hasError = true
            }

            // Email
            if (email.isEmpty() || !email.contains("@") || !email.contains(".")) {
                emailTitle.text = "Введите корректный email"
                emailTitle.setTextColor(errorTitleColor)
                emailInput.setHintTextColor(errorDimColor)
                emailInput.compoundDrawables[0]?.setColorFilter(errorDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            // Пароль
            if (password.isEmpty() || password.length < 6) {
                passwordTitle.text = "Пароль от 6 символов"
                passwordTitle.setTextColor(errorTitleColor)
                passwordInput.setHintTextColor(errorDimColor)
                eyeIcon.setColorFilter(errorDimColor)
                lockIcon.setColorFilter(errorDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            // Повтор пароля
            if (password2.isEmpty() || password2 != password) {
                password2Title.text = "Пароли не совпадают"
                password2Title.setTextColor(errorTitleColor)
                password2Input.setHintTextColor(errorDimColor)
                eyeIcon2.setColorFilter(errorDimColor)
                lockIcon2.setColorFilter(errorDimColor, android.graphics.PorterDuff.Mode.SRC_IN)
                hasError = true
            }

            if (!hasError) {
                val name = nameInput.text.toString().trim()
                val db = AppDatabase.getDatabase(this)
                val userDao = db.userDao()

                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                    val existingUser = userDao.getUserByEmail(email)

                    if (existingUser != null) {
                        Toast.makeText(this@RegisterActivity, "Этот email уже зарегистрирован", Toast.LENGTH_LONG).show()
                    } else {
                        // ХЕШИРУЕМ ПАРОЛЬ ПЕРЕД СОХРАНЕНИЕМ
                        val hashedPassword = PasswordHasher.hash(password)

                        val newUser = User(name = name, email = email, password = hashedPassword)
                        userDao.insertUser(newUser)

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
            finish() // MainActivity уже в стеке
        }
    }
}