package com.example.reviewbuddy.data.repositories

import com.example.reviewbuddy.data.models.Deck
import java.util.UUID

object DeckRepository {
    private val decks = mutableListOf<Deck>()

    init {
        // Initial mock data
        decks.add(Deck(UUID.randomUUID().toString(), "Data Structures", 45))
        decks.add(Deck(UUID.randomUUID().toString(), "Mobile Dev", 32))
        decks.add(Deck(UUID.randomUUID().toString(), "Science Technology...", 18))
    }

    fun getDecks(): List<Deck> {
        return decks.toList()
    }

    fun addDeck(title: String, cardCount: Int = 0): Deck {
        val newDeck = Deck(UUID.randomUUID().toString(), title, cardCount)
        decks.add(newDeck)
        return newDeck
    }

    fun removeDeck(deck: Deck) {
        decks.remove(deck)
    }
}
