package com.example.zabyvashka

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordHasher {

    // Хеширование пароля (cost 12 — хороший баланс для мобильных устройств)
    fun hash(password: String): String {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray())
    }

    // Проверка пароля при входе
    fun verify(password: String, storedHash: String): Boolean {
        return BCrypt.verifyer().verify(password.toCharArray(), storedHash).verified
    }
}