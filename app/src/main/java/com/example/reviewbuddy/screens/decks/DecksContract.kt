package com.example.reviewbuddy.screens.decks

import com.example.reviewbuddy.data.models.Deck

interface DecksContract {
    interface View {
        fun showDecks(decks: List<Deck>)
        fun toggleEmptyState(isEmpty: Boolean)
        fun showAddDeckDialog()
        fun showDeckOptionsDialog(deck: Deck)
        fun showDeckDetails(deck: Deck)
        fun showMessage(message: String)
        fun navigateToHome()
        fun navigateToProfile()
    }

    interface Presenter {
        fun loadDecks()
        fun onSearchQuery(query: String)
        fun onAddDeckClicked()
        fun confirmAddDeck(title: String)
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun confirmDeleteDeck(deck: Deck)
        fun onHomeClicked()
        fun onProfileClicked()
    }
}
