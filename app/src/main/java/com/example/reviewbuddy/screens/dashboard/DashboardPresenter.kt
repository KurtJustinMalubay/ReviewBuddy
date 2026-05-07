package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.data.repositories.DeckRepository

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    private val model = DashboardModel()

    override fun loadDecks() {
        view.showDecks(model.getDecks())
    }

    override fun onDeckClicked(deck: Deck) {
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        model.removeDeck(deck)
        view.showDeckRemovedMessage()
        loadDecks()
    }

    override fun onAddDeckClicked() {
        view.showAddDeckDialog()
    }

    override fun confirmAddDeck(title: String) {
        if (title.isNotBlank()) {
            model.addDeck(title.trim(), 0)
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
