package com.example.reviewbuddy.screens.decks

import com.example.reviewbuddy.data.models.Deck

interface DecksContract {
    interface View {
        fun showDecks(decks: List<Deck>)
        fun toggleEmptyState(isEmpty: Boolean)
        fun showAddDeckDialog()
        fun showDeckOptionsDialog(deck: Deck)
        fun showDeckDetails(deck: Deck)
        fun showMessage(message: String)
        fun navigateToHome()
        fun navigateToProfile()
        fun showFolderTabs(folders: List<String>, activeFolder: String?)
        fun showMultiSelectActionBar(visible: Boolean, count: Int)
        fun showFolderOrganizeDialog(deckIds: Set<String>, folders: List<String>)
    }

    interface Presenter {
        fun loadDecks()
        fun onSearchQuery(query: String)
        fun onAddDeckClicked()
        fun confirmAddDeck(title: String)
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun togglePinDeck(deck: Deck)
        fun deleteDecks(deckIds: Set<String>)
        fun moveDecksToFolder(deckIds: Set<String>, folder: String?)
        fun onFolderSelected(folder: String?)
        fun onHomeClicked()
        fun onProfileClicked()
    }
}
