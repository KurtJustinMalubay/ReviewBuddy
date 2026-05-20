package com.example.reviewbuddy.data.models

/**
 * Data model representing a registered user in the ReviewBuddy application.
 * Stores login credentials, dynamic profile photos, and account details.
 */
data class User(
    val username: String,
    val passwordHash: String, // Plain text or hashed password for simple local authentication
    val firstName: String,
    val middleName: String,
    val lastName: String,
    val email: String,
    val course: String = "",
    var avatarUri: String? = null // Persistable URI pointing to custom picked profile picture
)
