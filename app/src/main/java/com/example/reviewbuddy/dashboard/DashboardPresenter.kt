package com.example.reviewbuddy.dashboard

import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.data.repositories.DeckRepository

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    override fun loadDecks() {
        view.showDecks(DeckRepository.getDecks())
    }

    override fun onDeckClicked(deck: Deck) {
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        DeckRepository.removeDeck(deck)
        view.showDeckRemovedMessage()
        loadDecks()
    }

    override fun onAddDeckClicked() {
        DeckRepository.addDeck("New Custom Deck", 0)
        view.showDeckAddedMessage()
        loadDecks()
    }

    override fun onProfileClicked() {
        view.navigateToProfile()
    }

    override fun onLogoutClicked() {
        view.navigateToLogin()
    }
}
