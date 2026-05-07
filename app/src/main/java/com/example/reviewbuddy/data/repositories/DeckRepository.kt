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
    private val prefs: SharedPreferences =
        ReviewBuddyApp.appContext.getSharedPreferences("reviewbuddy_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val decks = mutableListOf<Deck>()

    // Tracks which user's decks are currently loaded in memory
    private var activeUsername: String = ""

    // -----------------------------------------------------------------
    // User-scoped key helpers
    // -----------------------------------------------------------------

    private fun currentUsername(): String =
        ReviewBuddyApp.userRepository.getLoggedInUser()?.username ?: ""

    private fun prefsKey(username: String): String = "decks_data_$username"

    /**
     * Call before any read/write operation.
     * If the logged-in user has changed since last access, flush current
     * in-memory list and reload from the new user's prefs key.
     */
    private fun ensureActiveUser() {
        val username = currentUsername()
        if (username != activeUsername) {
            activeUsername = username
            loadFromPrefs()
        }
    }

    // -----------------------------------------------------------------
    // Persistence helpers
    // -----------------------------------------------------------------

    private fun loadFromPrefs() {
        decks.clear()
        if (activeUsername.isBlank()) return

        val json = prefs.getString(prefsKey(activeUsername), null)
        if (json != null) {
            val type = object : TypeToken<List<Deck>>() {}.type
            val loaded: List<Deck> = gson.fromJson(json, type)
            decks.addAll(loaded)
        }
        // No mock seed data — each user starts with a clean slate
    }

    private fun saveToPrefs() {
        if (activeUsername.isBlank()) return
        prefs.edit().putString(prefsKey(activeUsername), gson.toJson(decks)).apply()
    }

    // -----------------------------------------------------------------
    // Public API
    // -----------------------------------------------------------------

    fun getDecks(): List<Deck> {
        ensureActiveUser()
        return decks.toList()
    }

    fun addDeck(title: String): Deck {
        ensureActiveUser()
        val newDeck = Deck(UUID.randomUUID().toString(), title)
        decks.add(newDeck)
        saveToPrefs()
        return newDeck
    }

    fun removeDeck(deck: Deck) {
        ensureActiveUser()
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
        ensureActiveUser()
        return decks.find { it.id == deckId }
    }

    fun addCard(deckId: String, front: String, back: String): Card? {
        ensureActiveUser()
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
        ensureActiveUser()
        val deck = getDeckById(deckId)
        val card = deck?.cards?.find { it.id == cardId }
        if (card != null) {
            card.front = newFront
            card.back = newBack
            saveToPrefs()
        }
    }

    fun deleteCard(deckId: String, cardId: String) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.cards.removeAll { it.id == cardId }
            saveToPrefs()
        }
    }
}
