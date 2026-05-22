package com.example.reviewbuddy.data.repositories

import android.content.Context
import android.content.SharedPreferences
import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Card
import com.example.reviewbuddy.data.models.Deck
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

/**
 * Repository class responsible for managing user-scoped Decks and flashcards data.
 * Automates multi-user data isolation and persists decks locally via SharedPreferences.
 */
class DeckRepository {
    private val prefs: SharedPreferences =
        ReviewBuddyApp.appContext.getSharedPreferences("reviewbuddy_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val decks = mutableListOf<Deck>()

    // Tracks which user's decks are currently loaded in memory to enforce correct data scope
    private var activeUsername: String = ""

    private fun currentUsername(): String =
        ReviewBuddyApp.userRepository.getLoggedInUser()?.username ?: ""

    private fun prefsKey(username: String): String = "decks_data_$username"

    /**
     * Guarantees data isolation. Flushes and reloads flashcard decks
     * if the logged-in user changes. Must be called prior to any operations.
     */
    private fun ensureActiveUser() {
        val username = currentUsername()
        if (username != activeUsername) {
            activeUsername = username
            loadFromPrefs()
        }
    }

    /**
     * Deserializes and loads flashcard decks for the current active user from SharedPreferences.
     */
    private fun loadFromPrefs() {
        decks.clear()
        if (activeUsername.isBlank()) return

        val json = prefs.getString(prefsKey(activeUsername), null)
        if (json != null) {
            val type = object : TypeToken<List<Deck>>() {}.type
            val loaded: List<Deck> = gson.fromJson(json, type)
            decks.addAll(loaded)
        }
    }

    /**
     * Serializes and writes the current in-memory decks list of the active user to SharedPreferences.
     */
    private fun saveToPrefs() {
        if (activeUsername.isBlank()) return
        prefs.edit().putString(prefsKey(activeUsername), gson.toJson(decks)).apply()
    }

    /**
     * Retrieves all decks belonging to the current logged-in user.
     */
    fun getDecks(): List<Deck> {
        ensureActiveUser()
        return decks.toList()
    }

    /**
     * Forces a complete reload of decks from SharedPreferences for the current user.
     * Use this after making changes to ensure in-memory cache is synced with disk.
     */
    fun reloadDecks() {
        if (activeUsername.isBlank()) return
        loadFromPrefs()
    }

    /**
     * Creates and adds a new empty Deck to the repository.
     */
    fun addDeck(title: String): Deck {
        ensureActiveUser()
        val newDeck = Deck(UUID.randomUUID().toString(), title)
        decks.add(newDeck)
        saveToPrefs()
        return newDeck
    }

    /**
     * Permanently deletes a single specific Deck from persistence.
     */
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

    /**
     * Finds a loaded Deck by its unique String ID.
     */
    fun getDeckById(deckId: String): Deck? {
        ensureActiveUser()
        return decks.find { it.id == deckId }
    }

    /**
     * Appends a new flashcard to a target Deck.
     */
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

    /**
     * Modifies the front/back text of an existing flashcard inside a deck.
     */
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

    /**
     * Deletes a card from a specific Deck by its unique card ID.
     */
    fun deleteCard(deckId: String, cardId: String) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.cards.removeAll { it.id == cardId }
            saveToPrefs()
        }
    }

    /**
     * Updates the pinned status of a deck. Pinned decks are styled on the dashboard.
     */
    fun updateDeckPinStatus(deckId: String, isPinned: Boolean) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.isPinned = isPinned
            saveToPrefs()
        }
    }

    /**
     * Records the current system timestamp as the deck's last accessed time (Recent Study Deck).
     */
    fun updateDeckLastAccessed(deckId: String) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.lastAccessed = System.currentTimeMillis()
            saveToPrefs()
        }
    }

    /**
     * Removes a deck from the dynamic "Recent Study Decks" list by resetting its lastAccessed timestamp.
     */
    fun clearDeckLastAccessed(deckId: String) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.lastAccessed = 0L
            saveToPrefs()
            
            // Force a complete reload to ensure data is fresh
            decks.clear()
            loadFromPrefs()
        }
    }

    /**
     * Places a deck into a named folder (or clears it if null is provided).
     */
    fun updateDeckFolder(deckId: String, folder: String?) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.folder = folder
            saveToPrefs()
        }
    }

    /**
     * Updates the name/title of a deck.
     */
    fun updateDeckTitle(deckId: String, newTitle: String) {
        ensureActiveUser()
        val deck = getDeckById(deckId)
        if (deck != null) {
            deck.title = newTitle
            saveToPrefs()
        }
    }

    /**
     * Performs bulk deletion of a set of decks (Multi-Select Delete).
     */
    fun removeDecks(deckIds: Set<String>) {
        ensureActiveUser()
        decks.removeAll { it.id in deckIds }
        saveToPrefs()
    }
}
