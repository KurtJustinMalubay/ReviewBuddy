package com.example.reviewbuddy.screens.decks

import com.example.reviewbuddy.data.models.Deck

class DecksPresenter(private val view: DecksContract.View) : DecksContract.Presenter {

    private val model = DecksModel()
    private var allDecks: List<Deck> = emptyList()

    override fun loadDecks() {
        allDecks = model.getDecks()
        view.showDecks(allDecks)
        view.toggleEmptyState(allDecks.isEmpty())
    }

    override fun onSearchQuery(query: String) {
        val filtered = if (query.isBlank()) {
            allDecks
        } else {
            allDecks.filter { it.title.contains(query, ignoreCase = true) }
        }
        view.showDecks(filtered)
        view.toggleEmptyState(filtered.isEmpty())
    }

    override fun onAddDeckClicked() {
        view.showAddDeckDialog()
    }

    override fun confirmAddDeck(title: String) {
        if (title.isNotBlank()) {
            model.addDeck(title.trim())
            view.showMessage("New deck added")
            loadDecks()
        }
    }

    override fun onDeckClicked(deck: Deck) {
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        view.showDeckOptionsDialog(deck)
    }

    override fun confirmDeleteDeck(deck: Deck) {
        model.removeDeck(deck)
        view.showMessage("Deck removed")
        loadDecks()
    }

    override fun onHomeClicked() {
        view.navigateToHome()
    }

    override fun onProfileClicked() {
        view.navigateToProfile()
    }
}
