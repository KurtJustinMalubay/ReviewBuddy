package com.example.reviewbuddy.screens.register

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.User

class RegisterModel {
    private val userRepository = ReviewBuddyApp.userRepository

    fun validateRegistration(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        pass: String,
        passConfirm: String
    ): Pair<Boolean, String> {
        if (username.isBlank() || firstName.isBlank() || lastName.isBlank() || email.isBlank() || pass.isBlank()) {
            return Pair(false, "All fields are required")
        }
        if (pass != passConfirm) {
            return Pair(false, "Passwords do not match")
        }
        if (userRepository.isUsernameTaken(username)) {
            return Pair(false, "Username is already taken")
        }
        return Pair(true, "Valid")
    }

    fun registerUser(user: User): Boolean {
        return userRepository.register(user)
    }
}
