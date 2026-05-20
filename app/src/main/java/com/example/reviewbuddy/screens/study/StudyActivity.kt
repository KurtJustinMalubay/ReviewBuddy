package com.example.reviewbuddy.screens.study

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.example.reviewbuddy.R

class StudyActivity : Activity(), StudyContract.View {

    private lateinit var presenter: StudyContract.Presenter

    private lateinit var textviewProgress: TextView
    private lateinit var progressBarStudy: ProgressBar
    private lateinit var cardContainer: FrameLayout
    private lateinit var textviewCardContent: TextView
    private lateinit var textviewCardLabel: TextView
    private lateinit var textviewTapHint: TextView
    private lateinit var layoutActionButtons: LinearLayout
    private lateinit var layoutCompletion: LinearLayout
    private lateinit var textviewCompletionMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.study_layout)

        presenter = StudyPresenter(this)

        textviewProgress = findViewById(R.id.textviewProgress)
        progressBarStudy = findViewById(R.id.progressBarStudy)
        cardContainer = findViewById(R.id.cardContainer)
        textviewCardContent = findViewById(R.id.textviewCardContent)
        textviewCardLabel = findViewById(R.id.textviewCardLabel)
        textviewTapHint = findViewById(R.id.textviewTapHint)
        layoutActionButtons = findViewById(R.id.layoutActionButtons)
        layoutCompletion = findViewById(R.id.layoutCompletion)
        textviewCompletionMessage = findViewById(R.id.textviewCompletionMessage)

        val buttonCloseStudy = findViewById<LinearLayout>(R.id.buttonCloseStudy)
        val buttonNeedsReview = findViewById<Button>(R.id.buttonNeedsReview)
        val buttonGotIt = findViewById<Button>(R.id.buttonGotIt)
        val buttonFinishStudy = findViewById<Button>(R.id.buttonFinishStudy)

        val deckId = intent.getStringExtra("DECK_ID")

        buttonCloseStudy.setOnClickListener { presenter.onCloseClicked() }
        buttonFinishStudy.setOnClickListener { presenter.onCloseClicked() }
        buttonNeedsReview.setOnClickListener { presenter.onNeedsReviewClicked() }
        buttonGotIt.setOnClickListener { presenter.onGotItClicked() }
        cardContainer.setOnClickListener { presenter.onCardTapped() }

        presenter.startStudySession(deckId)
    }

    override fun showCardFront(frontText: String) {
        layoutActionButtons.visibility = View.GONE
        textviewTapHint.visibility = View.VISIBLE
        textviewCardLabel.text = "FRONT"
        textviewCardContent.text = frontText
    }

    override fun showCardBack(backText: String) {
        textviewCardLabel.text = "BACK"
        textviewCardContent.text = backText
    }

    override fun flipToBack() {
        flipAnimation {
            layoutActionButtons.visibility = View.VISIBLE
            textviewTapHint.visibility = View.GONE
        }
    }

    override fun flipToFront() {
        flipAnimation {}
    }

    private fun flipAnimation(onMiddle: () -> Unit) {
        val flipOut = ObjectAnimator.ofFloat(cardContainer, "rotationY", 0f, 90f)
        flipOut.duration = 150
        flipOut.interpolator = AccelerateDecelerateInterpolator()

        val flipIn = ObjectAnimator.ofFloat(cardContainer, "rotationY", -90f, 0f)
        flipIn.duration = 150
        flipIn.interpolator = DecelerateInterpolator()

        flipOut.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onMiddle()
                flipIn.start()
            }
        })

        flipOut.start()
    }

    override fun updateProgress(current: Int, total: Int) {
        textviewProgress.text = "$current / $total"
        val progress = if (total > 0) ((current - 1).toFloat() / total * 100).toInt() else 0
        
        val progressAnimator = ObjectAnimator.ofInt(progressBarStudy, "progress", progressBarStudy.progress, progress)
        progressAnimator.duration = 300
        progressAnimator.start()
    }

    override fun showCompletionScreen(totalCards: Int) {
        progressBarStudy.progress = 100
        textviewProgress.text = "$totalCards / $totalCards"
        layoutActionButtons.visibility = View.GONE
        findViewById<View>(R.id.cardContainer).visibility = View.GONE
        
        layoutCompletion.visibility = View.VISIBLE
        textviewCompletionMessage.text = "You've reviewed $totalCards cards."
    }

    override fun closeStudySession() {
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
