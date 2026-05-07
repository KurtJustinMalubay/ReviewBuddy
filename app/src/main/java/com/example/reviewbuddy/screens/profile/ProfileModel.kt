package com.example.reviewbuddy.screens.profile

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.User

class ProfileModel {
    private val userRepository = ReviewBuddyApp.userRepository
    private val deckRepository = ReviewBuddyApp.deckRepository

    fun getUserProfile(): User? {
        return userRepository.getLoggedInUser()
    }

    fun getTotalDecks(): Int {
        return deckRepository.getDecks().size
    }

    fun getTotalCards(): Int {
        return deckRepository.getDecks().sumOf { it.cards.size }
    }
}
