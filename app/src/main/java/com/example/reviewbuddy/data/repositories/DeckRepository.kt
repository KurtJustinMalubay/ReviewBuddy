package com.example.reviewbuddy.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Card
import com.example.reviewbuddy.data.models.Deck
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class DeckRepository {
    private val prefs: SharedPreferences = ReviewBuddyApp.appContext.getSharedPreferences("reviewbuddy_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val decks = mutableListOf<Deck>()

    init {
        loadFromPrefs()
        if (decks.isEmpty()) {
            // Initial mock data if empty
            val initialDecks = listOf(
                Deck(UUID.randomUUID().toString(), "Data Structures"),
                Deck(UUID.randomUUID().toString(), "Mobile Dev"),
                Deck(UUID.randomUUID().toString(), "Science Technology...")
            )
            decks.addAll(initialDecks)
            saveToPrefs()
        }
    }

    private fun loadFromPrefs() {
        val json = prefs.getString("decks_data", null)
        if (json != null) {
            val type = object : TypeToken<List<Deck>>() {}.type
            val loadedDecks: List<Deck> = gson.fromJson(json, type)
            decks.clear()
            decks.addAll(loadedDecks)
        }
    }

    private fun saveToPrefs() {
        val json = gson.toJson(decks)
        prefs.edit().putString("decks_data", json).apply()
    }

    fun getDecks(): List<Deck> {
        return decks.toList()
    }

    fun addDeck(title: String, cardCount: Int = 0): Deck {
        val newDeck = Deck(UUID.randomUUID().toString(), title)
        decks.add(newDeck)
        saveToPrefs()
        return newDeck
    }

    fun removeDeck(deck: Deck) {
        val iterator = decks.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().id == deck.id) {
                iterator.remove()
                saveToPrefs()
                break
            }
        }
    }

    fun getDeckById(deckId: String): Deck? {
        return decks.find { it.id == deckId }
    }

    fun addCard(deckId: String, front: String, back: String): Card? {
        val deck = getDeckById(deckId)
        if (deck != null) {
            val card = Card(UUID.randomUUID().toString(), front, back)
            deck.cards.add(card)
            saveToPrefs()
            return card
        }
        return null
    }

    fun editCard(deckId: String, cardId: String, newFront: String, newBack: String) {
        val deck = getDeckById(deckId)
        val card = deck?.cards?.find { it.id == cardId }
        if (card != null) {
            card.front = newFront
            card.back = newBack
            saveToPrefs()
        }
    }

    fun deleteCard(deckId: String, cardId: String) {
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.cards.removeAll { it.id == cardId }
            saveToPrefs()
        }
    }
}
