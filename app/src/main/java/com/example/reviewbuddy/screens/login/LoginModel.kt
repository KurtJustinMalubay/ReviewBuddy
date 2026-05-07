package com.example.reviewbuddy.screens.login

import com.example.reviewbuddy.app.ReviewBuddyApp

class LoginModel {
    private val userRepository = ReviewBuddyApp.userRepository

    fun authenticate(username: String, password: String): Boolean {
        if (username.isBlank() || password.isBlank()) return false
        return userRepository.login(username, password)
    }
}
