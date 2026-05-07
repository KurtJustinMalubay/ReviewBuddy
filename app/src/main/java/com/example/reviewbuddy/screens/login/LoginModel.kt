package com.example.reviewbuddy.screens.login

class LoginModel {
    // Dummy authentication logic for testing
    fun authenticate(username: String, password: String): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }
}
