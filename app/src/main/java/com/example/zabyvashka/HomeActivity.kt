package com.example.zabyvashka

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var adapter: GoalAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val userName = intent.getStringExtra("USER_NAME") ?: "друг"

        val greetingText = findViewById<TextView>(R.id.greeting_text)
        val emptyText = findViewById<TextView>(R.id.empty_state_text)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        val recyclerView = findViewById<RecyclerView>(R.id.goals_recycler_view)

        greetingText.text = "Привет, $userName!"

        // Настраиваем список
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GoalAdapter(emptyList())
        recyclerView.adapter = adapter

        // Загружаем цели из БД
        val db = AppDatabase.getDatabase(this)
        val goalDao = db.goalDao()

        // Используем lifecycleScope — он автоматически отменяет корутину при закрытии экрана
        lifecycleScope.launch {
            goalDao.getAllGoals().collect { goalsList ->
                if (goalsList.isEmpty()) {
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyText.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    adapter.updateGoals(goalsList)
                }
            }
        }

        // Обработка нажатия на круглую кнопку "+"
        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddGoalActivity::class.java))
        }

        // Логика нижнего меню
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    greetingText.text = "Привет, $userName!"
                    // Если список пуст, показываем заглушку
                    if (adapter.itemCount == 0) {
                        emptyText.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        emptyText.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                    fabAdd.show()
                    true
                }
                R.id.nav_profile -> {
                    greetingText.text = "Профиль"
                    emptyText.text = "Пользователь: $userName\n\nЗдесь можно будет выйти из аккаунта."
                    emptyText.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                    fabAdd.hide()
                    true
                }
                else -> false
            }
        }
    }
}