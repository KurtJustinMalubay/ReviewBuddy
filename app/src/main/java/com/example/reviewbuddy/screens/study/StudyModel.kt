package com.example.reviewbuddy.screens.study

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Card

class StudyModel {
    private val repository = ReviewBuddyApp.deckRepository
    private var cardsQueue: MutableList<Card> = mutableListOf()
    private var totalCardsCount = 0

    fun initializeQueue(deckId: String) {
        val deck = repository.getDeckById(deckId)
        if (deck != null) {
            cardsQueue = deck.cards.shuffled().toMutableList()
            totalCardsCount = cardsQueue.size
        }
    }

    fun hasNextCard(): Boolean {
        return cardsQueue.isNotEmpty()
    }

    fun getNextCard(): Card? {
        return cardsQueue.firstOrNull()
    }

    fun removeCurrentCard() {
        if (cardsQueue.isNotEmpty()) {
            cardsQueue.removeAt(0)
        }
    }

    fun reinsertCurrentCard() {
        if (cardsQueue.isNotEmpty()) {
            val card = cardsQueue.removeAt(0)
            // Add it back to the end of the queue to review again
            cardsQueue.add(card)
        }
    }

    fun getTotalCards(): Int = totalCardsCount

    fun getRemainingCards(): Int = cardsQueue.size
}
