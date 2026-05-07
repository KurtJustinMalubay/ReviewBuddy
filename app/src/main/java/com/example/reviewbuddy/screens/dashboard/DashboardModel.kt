package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Deck

class DashboardModel {
    fun getDecks(): List<Deck> {
        return ReviewBuddyApp.deckRepository.getDecks()
    }

    fun addDeck(title: String, cards: Int) {
        ReviewBuddyApp.deckRepository.addDeck(title, cards)
    }

    fun removeDeck(deck: Deck) {
        ReviewBuddyApp.deckRepository.removeDeck(deck)
    }
}
