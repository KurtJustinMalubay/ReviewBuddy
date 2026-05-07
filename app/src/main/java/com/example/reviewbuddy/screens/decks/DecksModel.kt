package com.example.reviewbuddy.screens.decks

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Deck

class DecksModel {
    private val deckRepository = ReviewBuddyApp.deckRepository

    fun getDecks(): List<Deck> = deckRepository.getDecks()

    fun addDeck(title: String): Deck = deckRepository.addDeck(title)

    fun removeDeck(deck: Deck) = deckRepository.removeDeck(deck)
}
