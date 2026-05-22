package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    private val model = DashboardModel()
    private var allDecks: List<Deck> = emptyList()

    override fun loadDecks() {
        view.showUserGreeting("Hello, ${model.getUserFirstName()}")
        
        // Force complete refresh from repository by reloading from SharedPreferences
        com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.reloadDecks()
        allDecks = model.getDecks()
        
        // Sort decks: Pinned first, then Recents (lastAccessed > 0 and not pinned, descending)
        // NOTE: We do NOT display "rest" (lastAccessed == 0) on dashboard - only pinned + recent
        val pinned = allDecks.filter { it.isPinned }
        val recents = allDecks.filter { it.lastAccessed > 0L && !it.isPinned }
            .sortedByDescending { it.lastAccessed }
        
        val sortedList = pinned + recents  // Only pinned + recent, no "rest"
        
        view.showDashboardStats(model.getTotalDecks(), model.getTotalCards())
        val displayed = sortedList.take(4)
        
        view.showDecks(displayed)
        view.toggleEmptyState(displayed.isEmpty())
    }

    override fun onSearchQuery(query: String) {
        val filtered = if (query.isBlank()) {
            allDecks
        } else {
            allDecks.filter { it.title.contains(query, ignoreCase = true) }
        }
        
        // Apply same sorting to filtered results: pinned + recent only
        val pinned = filtered.filter { it.isPinned }
        val recents = filtered.filter { it.lastAccessed > 0L && !it.isPinned }
            .sortedByDescending { it.lastAccessed }
        
        val sorted = pinned + recents  // Only pinned + recent
        val displayed = sorted.take(4)
        view.showDecks(displayed)
        view.toggleEmptyState(filtered.isEmpty())
    }

    override fun onDeckClicked(deck: Deck) {
        com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.updateDeckLastAccessed(deck.id)
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        view.showDeckOptionsDialog(deck)
    }

    override fun togglePinDeck(deck: Deck) {
        com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.updateDeckPinStatus(deck.id, !deck.isPinned)
        loadDecks()
    }

    override fun removeFromRecents(deck: Deck) {
        com.example.reviewbuddy.app.ReviewBuddyApp.deckRepository.clearDeckLastAccessed(deck.id)
        view.showDeckRemovedMessage()
        
        // Post delay to ensure UI has time to settle before reloading
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            loadDecks()
        }, 100)
    }

    override fun onAddDeckClicked() {
        view.showAddDeckDialog()
    }

    override fun confirmAddDeck(title: String) {
        if (title.isNotBlank()) {
            model.addDeck(title.trim())
            view.showDeckAddedMessage()
            loadDecks()
        }
    }

    override fun onProfileClicked() {
        view.navigateToProfile()
    }

    override fun onDecksClicked() {
        view.navigateToDecks()
    }

    override fun onLogoutClicked() {
        view.navigateToLogin()
    }
}
