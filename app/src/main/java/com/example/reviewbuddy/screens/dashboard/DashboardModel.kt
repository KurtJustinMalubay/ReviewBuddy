package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Deck

class DashboardModel {
    fun getDecks(): List<Deck> {
        return ReviewBuddyApp.deckRepository.getDecks()
    }

    fun addDeck(title: String) {
        ReviewBuddyApp.deckRepository.addDeck(title)
    }

    fun removeDeck(deck: Deck) {
        ReviewBuddyApp.deckRepository.removeDeck(deck)
    }

    fun getUserFirstName(): String {
        return ReviewBuddyApp.userRepository.getLoggedInUser()?.firstName ?: "User"
    }

    fun getTotalDecks(): Int {
        return ReviewBuddyApp.deckRepository.getDecks().size
    }

    fun getTotalCards(): Int {
        return ReviewBuddyApp.deckRepository.getDecks().sumOf { it.cards.size }
    }
}
