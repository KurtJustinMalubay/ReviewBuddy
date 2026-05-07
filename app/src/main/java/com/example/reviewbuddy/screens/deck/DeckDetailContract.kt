package com.example.reviewbuddy.screens.deck

import com.example.reviewbuddy.data.models.Card

interface DeckDetailContract {
    interface View {
        fun showDeckTitle(title: String)
        fun navigateBack()
        fun showFeatureNotImplemented()
        fun showCards(cards: List<Card>)
        fun showAddCardDialog()
        fun showEditCardDialog(card: Card)
        fun showMessage(message: String)
        fun toggleEmptyState(isEmpty: Boolean)
    }

    interface Presenter {
        fun loadDeck(deckId: String?)
        fun onBackClicked()
        fun onAddCardClicked()
        fun onStudyClicked()
        fun confirmAddCard(front: String, back: String)
        fun onCardLongClicked(card: Card)
        fun confirmEditCard(cardId: String, newFront: String, newBack: String)
        fun confirmDeleteCard(cardId: String)
    }
}
