package com.example.reviewbuddy.screens.study

interface StudyContract {
    interface View {
        fun showCardFront(frontText: String)
        fun showCardBack(backText: String)
        fun flipToBack()
        fun flipToFront()
        fun updateProgress(current: Int, total: Int)
        fun showCompletionScreen(totalCards: Int)
        fun closeStudySession()
    }

    interface Presenter {
        fun startStudySession(deckId: String?)
        fun onCardTapped()
        fun onGotItClicked()
        fun onNeedsReviewClicked()
        fun onCloseClicked()
    }
}
