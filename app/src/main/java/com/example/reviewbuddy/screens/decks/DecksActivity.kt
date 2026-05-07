package com.example.reviewbuddy.screens.decks

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck
import com.example.reviewbuddy.screens.deck.DeckDetailActivity
import com.example.reviewbuddy.screens.profile.ProfileActivity

class DecksActivity : Activity(), DecksContract.View {

    private lateinit var presenter: DecksContract.Presenter
    private lateinit var recyclerViewDecks: RecyclerView
    private lateinit var deckAdapter: DecksListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.decks_layout)

        presenter = DecksPresenter(this)

        recyclerViewDecks = findViewById(R.id.recyclerViewDecks)
        recyclerViewDecks.layoutManager = LinearLayoutManager(this)

        deckAdapter = DecksListAdapter(
            emptyList(),
            onDeckClick = { deck -> presenter.onDeckClicked(deck) },
            onDeckLongClick = { deck -> presenter.onDeckLongClicked(deck) }
        )
        recyclerViewDecks.adapter = deckAdapter

        val edittextSearch = findViewById<EditText>(R.id.edittextSearch)
        val buttonAddDeck = findViewById<android.view.View>(R.id.buttonAddDeck)
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
        navHome.setOnClickListener { presenter.onHomeClicked() }
        navDecks.setOnClickListener { /* Already here */ }
        navProfile.setOnClickListener { presenter.onProfileClicked() }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadDecks()
    }

    override fun showDecks(decks: List<Deck>) {
        deckAdapter.updateDecks(decks)
    }

    override fun toggleEmptyState(isEmpty: Boolean) {
        val emptyState = findViewById<TextView>(R.id.textviewEmptyState)
        emptyState?.visibility = if (isEmpty) android.view.View.VISIBLE else android.view.View.GONE
        recyclerViewDecks.visibility = if (isEmpty) android.view.View.GONE else android.view.View.VISIBLE
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
            presenter.confirmAddDeck(edittextDeckTitle.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun showDeckOptionsDialog(deck: Deck) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Deck Options")
            .setMessage("What would you like to do with '${deck.title}'?")
            .setPositiveButton("Delete") { _, _ -> presenter.confirmDeleteDeck(deck) }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun showDeckDetails(deck: Deck) {
        val intent = Intent(this, DeckDetailActivity::class.java)
        intent.putExtra("DECK_ID", deck.id)
        intent.putExtra("DECK_TITLE", deck.title)
        startActivity(intent)
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHome() {
        finish()
    }

    override fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}
