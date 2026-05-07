package com.example.reviewbuddy.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.login.LoginActivity
import com.example.reviewbuddy.profile.ProfileActivity

class DashboardActivity : Activity(), DashboardContract.View {

    private lateinit val presenter: DashboardContract.Presenter
    private lateinit val listViewDecks: ListView
    private lateinit val deckAdapter: DeckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        presenter = DashboardPresenter(this)

        listViewDecks = findViewById(R.id.listViewDecks)
        val buttonProfile = findViewById<Button>(R.id.buttonProfile)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)
        val buttonAddDeck = findViewById<Button>(R.id.buttonAddDeck)

        listViewDecks.setOnItemClickListener { _, _, position, _ ->
            val deck = deckAdapter.getItem(position)
            if (deck != null) {
                presenter.onDeckClicked(deck)
            }
        }

        listViewDecks.setOnItemLongClickListener { _, _, position, _ ->
            val deck = deckAdapter.getItem(position)
            if (deck != null) {
                presenter.onDeckLongClicked(deck)
            }
            true
        }

        buttonAddDeck.setOnClickListener {
            presenter.onAddDeckClicked()
        }

        buttonProfile.setOnClickListener {
            presenter.onProfileClicked()
        }

        buttonLogout.setOnClickListener {
            presenter.onLogoutClicked()
        }

        presenter.loadDecks()
    }

    override fun showDecks(decks: List<Deck>) {
        deckAdapter = DeckAdapter(this, decks)
        listViewDecks.adapter = deckAdapter
    }

    override fun showDeckRemovedMessage() {
        Toast.makeText(this, "Deck removed", Toast.LENGTH_SHORT).show()
    }

    override fun showDeckAddedMessage() {
        Toast.makeText(this, "New deck added", Toast.LENGTH_SHORT).show()
    }

    override fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showDeckDetails(deck: Deck) {
        Toast.makeText(this, "Clicked on: ${deck.title}", Toast.LENGTH_SHORT).show()
    }
}
