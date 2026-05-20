package com.example.reviewbuddy.screens.decks

import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.app.ReviewBuddyApp

class DecksPresenter(private val view: DecksContract.View) : DecksContract.Presenter {

    private val model = DecksModel()
    private var allDecks: List<Deck> = emptyList()
    private var activeFolder: String? = null
    private var currentQuery: String = ""

    override fun loadDecks() {
        allDecks = model.getDecks()
        
        // Extract unique, sorted list of non-blank folders
        val folders = allDecks.mapNotNull { it.folder }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        view.showFolderTabs(folders, activeFolder)

        // Filter and sort decks
        val filtered = allDecks.filter { deck ->
            val matchesFolder = if (activeFolder == null) true else deck.folder == activeFolder
            val matchesQuery = if (currentQuery.isBlank()) true else deck.title.contains(currentQuery, ignoreCase = true)
            matchesFolder && matchesQuery
        }

        // Sort decks: Pinned first, then sorted by Title alphabetically
        val sorted = filtered.sortedWith(compareByDescending<Deck> { it.isPinned }.thenBy { it.title.lowercase() })

        view.showDecks(sorted)
        view.toggleEmptyState(sorted.isEmpty())
    }

    override fun onSearchQuery(query: String) {
        currentQuery = query
        loadDecks()
    }

    override fun onAddDeckClicked() {
        view.showAddDeckDialog()
    }

    override fun confirmAddDeck(title: String) {
        if (title.isNotBlank()) {
            val newDeck = model.addDeck(title.trim())
            // Automatically assign to current folder filter if on a folder tab!
            if (!activeFolder.isNullOrBlank()) {
                ReviewBuddyApp.deckRepository.updateDeckFolder(newDeck.id, activeFolder)
            }
            view.showMessage("New deck added")
            loadDecks()
        }
    }

    override fun onDeckClicked(deck: Deck) {
        ReviewBuddyApp.deckRepository.updateDeckLastAccessed(deck.id)
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        view.showDeckOptionsDialog(deck)
    }

    override fun togglePinDeck(deck: Deck) {
        ReviewBuddyApp.deckRepository.updateDeckPinStatus(deck.id, !deck.isPinned)
        loadDecks()
    }

    override fun deleteDecks(deckIds: Set<String>) {
        ReviewBuddyApp.deckRepository.removeDecks(deckIds)
        view.showMessage("${deckIds.size} decks deleted")
        loadDecks()
    }

    override fun moveDecksToFolder(deckIds: Set<String>, folder: String?) {
        val sanitizedFolder = if (folder.isNullOrBlank()) null else folder.trim()
        deckIds.forEach { deckId ->
            ReviewBuddyApp.deckRepository.updateDeckFolder(deckId, sanitizedFolder)
        }
        view.showMessage("Moved ${deckIds.size} decks to ${sanitizedFolder ?: "All"}")
        loadDecks()
    }

    override fun onFolderSelected(folder: String?) {
        activeFolder = folder
        loadDecks()
    }

    override fun onHomeClicked() {
        view.navigateToHome()
    }

    override fun onProfileClicked() {
        view.navigateToProfile()
    }
}
