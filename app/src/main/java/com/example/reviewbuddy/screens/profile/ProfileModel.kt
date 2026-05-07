package com.example.reviewbuddy.screens.profile

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.User

class ProfileModel {
    private val userRepository = ReviewBuddyApp.userRepository

    fun getUserProfile(): User? {
        return userRepository.getLoggedInUser()
    }
}
