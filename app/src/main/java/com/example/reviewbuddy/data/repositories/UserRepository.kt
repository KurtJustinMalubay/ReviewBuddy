package com.example.reviewbuddy.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Repository class responsible for managing User persistence via SharedPreferences.
 * Provides credentials check, registrations, auto-logins, and profile updates.
 */
class UserRepository {
    private val prefs: SharedPreferences = ReviewBuddyApp.appContext.getSharedPreferences("users_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val USERS_KEY = "saved_users"
    private val LOGGED_IN_USER_KEY = "logged_in_username"

    /**
     * Retrieves all saved users map from local storage.
     */
    private fun getUsers(): MutableMap<String, User> {
        val usersJson = prefs.getString(USERS_KEY, null)
        if (usersJson != null) {
            val type = object : TypeToken<MutableMap<String, User>>() {}.type
            return gson.fromJson(usersJson, type)
        }
        return mutableMapOf()
    }

    /**
     * Saves the entire map of users to local SharedPreferences.
     */
    private fun saveUsers(users: Map<String, User>) {
        prefs.edit().putString(USERS_KEY, gson.toJson(users)).apply()
    }

    /**
     * Checks if a specific username is already registered in the application.
     */
    fun isUsernameTaken(username: String): Boolean {
        return getUsers().containsKey(username)
    }

    /**
     * Registers a new User, saves them to SharedPreferences, and auto-logs them in.
     * Returns true if successful, false if the username is already occupied.
     */
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

    /**
     * Authenticates a user. If credentials match, sets them as the active logged-in user.
     */
    fun login(username: String, passwordHash: String): Boolean {
        val users = getUsers()
        val user = users[username]
        if (user != null && user.passwordHash == passwordHash) {
            prefs.edit().putString(LOGGED_IN_USER_KEY, username).apply()
            return true
        }
        return false
    }

    /**
     * Logouts the active user by removing their login session token key.
     */
    fun logout() {
        prefs.edit().remove(LOGGED_IN_USER_KEY).apply()
    }

    /**
     * Retrieves the currently logged-in user, or null if no user is active.
     */
    fun getLoggedInUser(): User? {
        val username = prefs.getString(LOGGED_IN_USER_KEY, null) ?: return null
        return getUsers()[username]
    }

    /**
     * Updates and persists the custom profile image URI for a specific username.
     */
    fun updateUserAvatar(username: String, avatarUri: String?) {
        val users = getUsers()
        val user = users[username]
        if (user != null) {
            user.avatarUri = avatarUri
            saveUsers(users)
        }
    }
}
