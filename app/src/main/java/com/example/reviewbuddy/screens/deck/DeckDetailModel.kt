package com.example.reviewbuddy.screens.deck

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Card
import com.example.reviewbuddy.data.models.Deck

class DeckDetailModel {
    private val repository = ReviewBuddyApp.deckRepository

    fun getDeck(deckId: String): Deck? {
        return repository.getDeckById(deckId)
    }

    fun getCards(deckId: String): List<Card> {
        return getDeck(deckId)?.cards ?: emptyList()
    }

    fun addCard(deckId: String, front: String, back: String): Boolean {
        return repository.addCard(deckId, front, back) != null
    }

    fun editCard(deckId: String, cardId: String, newFront: String, newBack: String) {
        repository.editCard(deckId, cardId, newFront, newBack)
    }

    fun deleteCard(deckId: String, cardId: String) {
        repository.deleteCard(deckId, cardId)
    }
}
