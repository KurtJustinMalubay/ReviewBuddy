package com.example.reviewbuddy.screens.deck

import com.example.reviewbuddy.data.models.Card

class DeckDetailPresenter(private val view: DeckDetailContract.View) : DeckDetailContract.Presenter {

    private val model = DeckDetailModel()
    private var currentDeckId: String? = null

    override fun loadDeck(deckId: String?) {
        currentDeckId = deckId
        if (deckId != null) {
            val deck = model.getDeck(deckId)
            if (deck != null) {
                view.showDeckTitle(deck.title)
                refreshCards()
            }
        }
    }

    private fun refreshCards() {
        val deckId = currentDeckId ?: return
        val cards = model.getCards(deckId)
        view.showCards(cards)
        view.toggleEmptyState(cards.isEmpty())
    }

    override fun onBackClicked() {
        view.navigateBack()
    }

    override fun onAddCardClicked() {
        view.showAddCardDialog()
    }

    override fun confirmAddCard(front: String, back: String) {
        val deckId = currentDeckId ?: return
        if (front.isNotBlank() && back.isNotBlank()) {
            val success = model.addCard(deckId, front.trim(), back.trim())
            if (success) {
                view.showMessage("Flashcard added")
                refreshCards()
            }
        } else {
            view.showMessage("Fields cannot be empty")
        }
    }

    override fun onCardLongClicked(card: Card) {
        view.showEditCardDialog(card)
    }

    override fun confirmEditCard(cardId: String, newFront: String, newBack: String) {
        val deckId = currentDeckId ?: return
        if (newFront.isNotBlank() && newBack.isNotBlank()) {
            model.editCard(deckId, cardId, newFront.trim(), newBack.trim())
            view.showMessage("Flashcard updated")
            refreshCards()
        } else {
            view.showMessage("Fields cannot be empty")
        }
    }

    override fun confirmDeleteCard(cardId: String) {
        val deckId = currentDeckId ?: return
        model.deleteCard(deckId, cardId)
        view.showMessage("Flashcard deleted")
        refreshCards()
    }

    override fun onStudyClicked() {
        val deckId = currentDeckId ?: return
        val cards = model.getCards(deckId)
        if (cards.isEmpty()) {
            view.showMessage("Add some cards first to study")
        } else {
            view.launchStudyMode(deckId)
        }
    }
}
