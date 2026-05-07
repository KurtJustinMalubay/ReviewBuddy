package com.example.reviewbuddy.data.models

data class User(
    val username: String,
    val passwordHash: String, // Keeping it simple for local memory, will just store password directly for now
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val course: String = ""
)
