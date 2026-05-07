package com.example.reviewbuddy.screens.register

class RegisterModel {
    fun validateRegistration(username: String, pass: String, passConfirm: String): Boolean {
        return username.isNotBlank() && pass.isNotBlank() && pass == passConfirm
    }
}
