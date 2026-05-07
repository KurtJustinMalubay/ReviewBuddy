package com.example.reviewbuddy.screens.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.screens.login.LoginActivity
import com.example.reviewbuddy.screens.profile.ProfileActivity

class DashboardActivity : Activity(), DashboardContract.View {

    private lateinit var presenter: DashboardContract.Presenter
    private lateinit var listViewDecks: ListView
    private lateinit var deckAdapter: DeckAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        presenter = DashboardPresenter(this)

        listViewDecks = findViewById(R.id.listViewDecks)
        val buttonProfile = findViewById<android.view.View>(R.id.buttonProfile)
        val buttonLogout = findViewById<android.view.View>(R.id.buttonLogout)
        val buttonAddDeck = findViewById<android.view.View>(R.id.buttonAddDeck)

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

    }

    override fun onResume() {
        super.onResume()
        presenter.loadDecks()
    }

    override fun showDecks(decks: List<Deck>) {
        deckAdapter = DeckAdapter(this, decks)
        listViewDecks.adapter = deckAdapter
    }

    override fun showUserGreeting(name: String) {
        val greetingText = findViewById<android.widget.TextView>(R.id.textviewGreeting)
        greetingText?.text = name
    }

    override fun toggleEmptyState(isEmpty: Boolean) {
        val emptyStateText = findViewById<android.widget.TextView>(R.id.textviewDashboardEmptyState)
        emptyStateText?.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        listViewDecks.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
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
        val intent = Intent(this, com.example.reviewbuddy.screens.deck.DeckDetailActivity::class.java)
        intent.putExtra("DECK_ID", deck.id)
        intent.putExtra("DECK_TITLE", deck.title)
        startActivity(intent)
    }

    override fun showAddDeckDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_add_deck)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val edittextDeckTitle = dialog.findViewById<android.widget.EditText>(R.id.edittextDeckTitle)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonCreate = dialog.findViewById<Button>(R.id.buttonCreate)

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        buttonCreate.setOnClickListener {
            val title = edittextDeckTitle.text.toString()
            presenter.confirmAddDeck(title)
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun showDeckOptionsDialog(deck: Deck) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Deck Options")
            .setMessage("What would you like to do with '${deck.title}'?")
            .setPositiveButton("Delete") { _, _ ->
                presenter.confirmDeleteDeck(deck)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
