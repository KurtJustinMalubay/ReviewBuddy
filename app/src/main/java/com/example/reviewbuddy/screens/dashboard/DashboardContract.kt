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
    }

    interface Presenter {
        fun loadDecks()
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun onAddDeckClicked()
        fun onProfileClicked()
        fun onLogoutClicked()
    }
}
