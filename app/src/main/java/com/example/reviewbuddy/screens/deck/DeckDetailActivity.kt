package com.example.reviewbuddy.screens.deck

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.reviewbuddy.R

class DeckDetailActivity : Activity(), DeckDetailContract.View {

    private lateinit var presenter: DeckDetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deck_detail_layout)

        presenter = DeckDetailPresenter(this)

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        val buttonAddCard = findViewById<Button>(R.id.buttonAddCard)
        val buttonStudy = findViewById<Button>(R.id.buttonStudy)

        val title = intent.getStringExtra("DECK_TITLE")
        presenter.loadDeck(title)

        buttonBack.setOnClickListener {
            presenter.onBackClicked()
        }

        buttonAddCard.setOnClickListener {
            presenter.onAddCardClicked()
        }

        buttonStudy.setOnClickListener {
            presenter.onStudyClicked()
        }
    }

    override fun showDeckTitle(title: String) {
        findViewById<TextView>(R.id.textviewDeckTitle).text = title
    }

    override fun navigateBack() {
        finish()
    }

    override fun showFeatureNotImplemented() {
        Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show()
    }
}
