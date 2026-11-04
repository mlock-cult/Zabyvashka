package com.example.zabyvashka

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddGoalActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        val titleInput = findViewById<EditText>(R.id.edit_goal_title)
        val timeInput = findViewById<EditText>(R.id.edit_goal_time)
        val saveButton = findViewById<Button>(R.id.btn_save_goal)

        saveButton.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val time = timeInput.text.toString().trim()

            if (title.isNotEmpty() && time.isNotEmpty()) {
                val db = AppDatabase.getDatabase(this)
                val goalDao = db.goalDao()

                lifecycleScope.launch {
                    // Выполняем запись в фоновом потоке
                    withContext(Dispatchers.IO) {
                        val newGoal = Goal(title = title, time = time, userId = 1)
                        goalDao.insertGoal(newGoal)
                    }
                    // Закрываем экран ТОЛЬКО после того, как запись завершена
                    finish()
                }
            }
        }
    }
}