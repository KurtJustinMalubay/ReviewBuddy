package com.example.reviewbuddy.screens.deck

interface DeckDetailContract {
    interface View {
        fun showDeckTitle(title: String)
        fun navigateBack()
        fun showFeatureNotImplemented()
    }

    interface Presenter {
        fun loadDeck(title: String?)
        fun onBackClicked()
        fun onAddCardClicked()
        fun onStudyClicked()
    }
}
