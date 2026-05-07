package com.example.reviewbuddy.screens.study

import com.example.reviewbuddy.data.models.Card

class StudyPresenter(private val view: StudyContract.View) : StudyContract.Presenter {

    private val model = StudyModel()
    private var isShowingBack = false
    private var currentCard: Card? = null

    override fun startStudySession(deckId: String?) {
        if (deckId != null) {
            model.initializeQueue(deckId)
            if (model.getTotalCards() > 0) {
                showNextCard()
            } else {
                view.showCompletionScreen(0)
            }
        } else {
            view.closeStudySession()
        }
    }

    private fun showNextCard() {
        if (model.hasNextCard()) {
            isShowingBack = false
            currentCard = model.getNextCard()
            currentCard?.let {
                view.showCardFront(it.front)
                view.flipToFront()
            }
            updateProgressView()
        } else {
            view.showCompletionScreen(model.getTotalCards())
        }
    }

    private fun updateProgressView() {
        val total = model.getTotalCards()
        val remaining = model.getRemainingCards()
        val current = total - remaining + 1
        val displayCurrent = if (current > total) total else current
        view.updateProgress(displayCurrent, total)
    }

    override fun onCardTapped() {
        if (currentCard == null) return

        if (!isShowingBack) {
            isShowingBack = true
            currentCard?.let {
                view.showCardBack(it.back)
                view.flipToBack()
            }
        }
    }

    override fun onGotItClicked() {
        if (!isShowingBack) return
        model.removeCurrentCard()
        showNextCard()
    }

    override fun onNeedsReviewClicked() {
        if (!isShowingBack) return
        model.reinsertCurrentCard()
        showNextCard()
    }

    override fun onCloseClicked() {
        view.closeStudySession()
    }
}
