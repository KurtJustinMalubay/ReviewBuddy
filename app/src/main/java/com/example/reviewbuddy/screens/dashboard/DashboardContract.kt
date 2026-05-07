package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck

interface DashboardContract {
    interface View {
        fun showDecks(decks: List<Deck>)
        fun showDeckRemovedMessage()
        fun showDeckAddedMessage()
        fun navigateToProfile()
        fun navigateToDecks()
        fun navigateToLogin()
        fun showDeckDetails(deck: Deck)
        fun showAddDeckDialog()
        fun showDeckOptionsDialog(deck: Deck)
        fun showUserGreeting(name: String)
        fun toggleEmptyState(isEmpty: Boolean)
    }

    interface Presenter {
        fun loadDecks()
        fun onSearchQuery(query: String)
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun confirmDeleteDeck(deck: Deck)
        fun onAddDeckClicked()
        fun confirmAddDeck(title: String)
        fun onProfileClicked()
        fun onDecksClicked()
        fun onLogoutClicked()
    }
}
