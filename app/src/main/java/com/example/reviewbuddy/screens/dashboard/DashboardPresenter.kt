package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.data.repositories.DeckRepository

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    private val model = DashboardModel()

    override fun loadDecks() {
        view.showUserGreeting("Hello, ${model.getUserFirstName()}")
        val decks = model.getDecks()
        view.showDecks(decks)
        view.toggleEmptyState(decks.isEmpty())
    }

    override fun onDeckClicked(deck: Deck) {
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        view.showDeckOptionsDialog(deck)
    }

    override fun confirmDeleteDeck(deck: Deck) {
        model.removeDeck(deck)
        view.showDeckRemovedMessage()
        loadDecks()
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

    override fun onLogoutClicked() {
        view.navigateToLogin()
    }
}
