package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck

interface DashboardContract {
    interface View {
        fun showDecks(decks: List<Deck>)
        fun showDeckRemovedMessage()
        fun showDeckAddedMessage()
        fun navigateToProfile()
        fun navigateToLogin()
        fun showDeckDetails(deck: Deck)
        fun showAddDeckDialog()
    }

    interface Presenter {
        fun loadDecks()
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun onAddDeckClicked() // changed to trigger dialog
        fun confirmAddDeck(title: String)
        fun onProfileClicked()
        fun onLogoutClicked()
    }
}
