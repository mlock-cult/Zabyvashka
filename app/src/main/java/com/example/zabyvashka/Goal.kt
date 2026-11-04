package com.example.zabyvashka

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goals")
data class Goal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, // Например, "Проверить утюг"
    val time: String,  // Время, например "08:00"
    val userId: Long   // Привязка к пользователю (пока не используем, но на будущее)
)