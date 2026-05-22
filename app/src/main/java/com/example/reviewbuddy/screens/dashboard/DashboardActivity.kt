package com.example.reviewbuddy.screens.dashboard

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.screens.decks.DecksActivity
import com.example.reviewbuddy.screens.login.LoginActivity
import com.example.reviewbuddy.screens.profile.ProfileActivity

class DashboardActivity : Activity(), DashboardContract.View {

    private lateinit var presenter: DashboardContract.Presenter
    private lateinit var recyclerViewDecks: RecyclerView
    private lateinit var deckAdapter: DeckRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)

        presenter = DashboardPresenter(this)

        recyclerViewDecks = findViewById(R.id.recyclerViewDecks)
        recyclerViewDecks.layoutManager = GridLayoutManager(this, 2)

        deckAdapter = DeckRecyclerViewAdapter(
            emptyList(),
            onDeckClick = { deck -> presenter.onDeckClicked(deck) },
            onDeckLongClick = { deck -> presenter.onDeckLongClicked(deck) }
        )
        recyclerViewDecks.adapter = deckAdapter

        val edittextSearch = findViewById<EditText>(R.id.edittextSearch)
        val buttonAddDeck = findViewById<android.view.View>(R.id.buttonAddDeck)
        val buttonSeeAll = findViewById<android.view.View>(R.id.buttonSeeAll)
        val navHome = findViewById<android.view.View>(R.id.navHome)
        val navDecks = findViewById<android.view.View>(R.id.navDecks)
        val navProfile = findViewById<android.view.View>(R.id.navProfile)

        edittextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                presenter.onSearchQuery(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        buttonAddDeck.setOnClickListener { presenter.onAddDeckClicked() }
        buttonSeeAll.setOnClickListener { presenter.onDecksClicked() }
        navHome.setOnClickListener { /* Already on Home */ }
        navDecks.setOnClickListener { presenter.onDecksClicked() }
        navProfile.setOnClickListener { presenter.onProfileClicked() }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDecks()
    }

    override fun showDecks(decks: List<Deck>) {
        deckAdapter.updateDecks(decks)
    }

    override fun showUserGreeting(name: String) {
        val greetingText = findViewById<android.widget.TextView>(R.id.textviewGreeting)
        val firstName = name.replace("Hello, ", "").trim()
        greetingText?.text = "$firstName 👋"
    }

    override fun toggleEmptyState(isEmpty: Boolean) {
        val emptyStateText = findViewById<android.widget.TextView>(R.id.textviewDashboardEmptyState)
        emptyStateText?.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        recyclerViewDecks.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
    }

    override fun closeDialog() {
        // Will be called to close any open dialogs
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
        overridePendingTransition(0, 0)
    }

    override fun navigateToDecks() {
        val intent = Intent(this, DecksActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
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
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_deck)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val edittextDeckTitle = dialog.findViewById<EditText>(R.id.edittextDeckTitle)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancel)
        val buttonCreate = dialog.findViewById<Button>(R.id.buttonCreate)

        buttonCancel.setOnClickListener { dialog.dismiss() }
        buttonCreate.setOnClickListener {
            val title = edittextDeckTitle.text.toString()
            presenter.confirmAddDeck(title)
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showDashboardStats(deckCount: Int, cardCount: Int) {
        findViewById<android.widget.TextView>(R.id.textviewDashboardDecks)?.text = deckCount.toString()
        findViewById<android.widget.TextView>(R.id.textviewDashboardCards)?.text = cardCount.toString()
    }

    override fun showDeckOptionsDialog(deck: Deck) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_deck_options)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val dialogTitle = dialog.findViewById<android.widget.TextView>(R.id.dialogTitle)
        val dialogSubtitle = dialog.findViewById<android.widget.TextView>(R.id.dialogSubtitle)
        val textPinLabel = dialog.findViewById<android.widget.TextView>(R.id.textPinLabel)
        val rowPin = dialog.findViewById<android.view.View>(R.id.rowPin)
        val rowActionSecondary = dialog.findViewById<android.view.View>(R.id.rowActionSecondary)
        val textSecondaryLabel = dialog.findViewById<android.widget.TextView>(R.id.textSecondaryLabel)
        val rowDelete = dialog.findViewById<android.view.View>(R.id.rowDelete)
        val rowMultiSelect = dialog.findViewById<android.view.View>(R.id.rowMultiSelect)
        val buttonCancel = dialog.findViewById<android.widget.Button>(R.id.buttonCancel)

        dialogTitle.text = deck.title
        dialogSubtitle.text = "Manage options for this deck"

        // Set Pin / Unpin state
        textPinLabel.text = if (deck.isPinned) "Unpin Deck" else "Pin Deck"
        rowPin.setOnClickListener {
            presenter.togglePinDeck(deck)
            dialog.dismiss()
        }

        // Dashboard only: "Remove from Recents"
        textSecondaryLabel.text = "Remove from Recents"
        rowActionSecondary.setOnClickListener {
            presenter.removeFromRecents(deck)
            dialog.dismiss()
        }

        // Hide "Delete Permanently" and "Multi-Select" rows on dashboard
        rowDelete.visibility = android.view.View.GONE
        rowMultiSelect?.visibility = android.view.View.GONE

        buttonCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
