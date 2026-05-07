package com.example.reviewbuddy.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserRepository {
    private val prefs: SharedPreferences = ReviewBuddyApp.appContext.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val USERS_KEY = "saved_users"
    private val LOGGED_IN_USER_KEY = "logged_in_username"

    private fun getUsers(): MutableMap<String, User> {
        val usersJson = prefs.getString(USERS_KEY, null)
        if (usersJson != null) {
            val type = object : TypeToken<MutableMap<String, User>>() {}.type
            return gson.fromJson(usersJson, type)
        }
        return mutableMapOf()
    }

    private fun saveUsers(users: Map<String, User>) {
        prefs.edit().putString(USERS_KEY, gson.toJson(users)).apply()
    }

    fun isUsernameTaken(username: String): Boolean {
        return getUsers().containsKey(username)
    }

    fun register(user: User): Boolean {
        val users = getUsers()
        if (users.containsKey(user.username)) {
            return false // Username already taken
        }
        users[user.username] = user
        saveUsers(users)
        
        // Auto-login upon successful registration
        prefs.edit().putString(LOGGED_IN_USER_KEY, user.username).apply()
        return true
    }

    fun login(username: String, passwordHash: String): Boolean {
        val users = getUsers()
        val user = users[username]
        if (user != null && user.passwordHash == passwordHash) {
            prefs.edit().putString(LOGGED_IN_USER_KEY, username).apply()
            return true
        }
        return false
    }

    fun logout() {
        prefs.edit().remove(LOGGED_IN_USER_KEY).apply()
    }

    fun getLoggedInUser(): User? {
        val username = prefs.getString(LOGGED_IN_USER_KEY, null) ?: return null
        return getUsers()[username]
    }
}
