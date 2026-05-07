package com.example.reviewbuddy.screens.deck

class DeckDetailPresenter(private val view: DeckDetailContract.View) : DeckDetailContract.Presenter {

    private val model = DeckDetailModel()

    override fun loadDeck(title: String?) {
        val deckTitle = title ?: "Unknown Deck"
        view.showDeckTitle(deckTitle)
    }

    override fun onBackClicked() {
        view.navigateBack()
    }

    override fun onAddCardClicked() {
        view.showFeatureNotImplemented()
    }

    override fun onStudyClicked() {
        view.showFeatureNotImplemented()
    }
}
