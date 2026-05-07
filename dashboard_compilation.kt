// =============================================================================
//  ReviewBuddy — Dashboard Screen  |  Kotlin Source Compilation
//  Generated: 2026-05-07
//  Includes: DashboardContract · DashboardModel · DashboardPresenter
//            DeckAdapter · DashboardActivity
// =============================================================================


// ─────────────────────────────────────────────────────────────────────────────
// FILE 1 of 5 │ DashboardContract.kt
// ─────────────────────────────────────────────────────────────────────────────

package com.example.reviewbuddy.screens.dashboard

import com.example.reviewbuddy.data.models.Deck

interface DashboardContract {
    interface View {
        fun showDecks(decks: List<Deck>)
        fun showDeckRemovedMessage()
        fun showDeckAddedMessage()
        fun navigateToProfile()
        fun navigateToLogin()
        fun showDeckDetails(deck: Deck)
        fun showAddDeckDialog()
        fun showDeckOptionsDialog(deck: Deck)
        fun showUserGreeting(name: String)
        fun toggleEmptyState(isEmpty: Boolean)
    }

    interface Presenter {
        fun loadDecks()
        fun onDeckClicked(deck: Deck)
        fun onDeckLongClicked(deck: Deck)
        fun confirmDeleteDeck(deck: Deck)
        fun onAddDeckClicked()
        fun confirmAddDeck(title: String)
        fun onProfileClicked()
        fun onLogoutClicked()
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// FILE 2 of 5 │ DashboardModel.kt
// ─────────────────────────────────────────────────────────────────────────────

// package com.example.reviewbuddy.screens.dashboard   ← same package

import com.example.reviewbuddy.app.ReviewBuddyApp
import com.example.reviewbuddy.data.models.Deck

class DashboardModel {
    fun getDecks(): List<Deck> {
        return ReviewBuddyApp.deckRepository.getDecks()
    }

    fun addDeck(title: String) {
        ReviewBuddyApp.deckRepository.addDeck(title)
    }

    fun removeDeck(deck: Deck) {
        ReviewBuddyApp.deckRepository.removeDeck(deck)
    }

    fun getUserFirstName(): String {
        return ReviewBuddyApp.userRepository.getLoggedInUser()?.firstName ?: "User"
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// FILE 3 of 5 │ DashboardPresenter.kt
// ─────────────────────────────────────────────────────────────────────────────

// package com.example.reviewbuddy.screens.dashboard   ← same package

import com.example.reviewbuddy.data.models.Deck

class DashboardPresenter(private val view: DashboardContract.View) : DashboardContract.Presenter {

    private val model = DashboardModel()

    override fun loadDecks() {
        view.showUserGreeting("Hello, ${model.getUserFirstName()}")
        val decks = model.getDecks()
        view.showDecks(decks)
        view.toggleEmptyState(decks.isEmpty())
    }

    override fun onDeckClicked(deck: Deck) {
        view.showDeckDetails(deck)
    }

    override fun onDeckLongClicked(deck: Deck) {
        view.showDeckOptionsDialog(deck)
    }

    override fun confirmDeleteDeck(deck: Deck) {
        model.removeDeck(deck)
        view.showDeckRemovedMessage()
        loadDecks()
    }

    override fun onAddDeckClicked() {
        view.showAddDeckDialog()
    }

    override fun confirmAddDeck(title: String) {
        if (title.isNotBlank()) {
            model.addDeck(title.trim())
            view.showDeckAddedMessage()
            loadDecks()
        }
    }

    override fun onProfileClicked() {
        view.navigateToProfile()
    }

    override fun onLogoutClicked() {
        view.navigateToLogin()
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// FILE 4 of 5 │ DeckAdapter.kt
// ─────────────────────────────────────────────────────────────────────────────

// package com.example.reviewbuddy.screens.dashboard   ← same package

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck

class DeckAdapter(context: Context, private val decks: List<Deck>) :
    ArrayAdapter<Deck>(context, 0, decks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_deck, parent, false)
        }

        val deck = decks[position]

        val titleText = view!!.findViewById<TextView>(R.id.textTitle)
        val countText = view.findViewById<TextView>(R.id.textCount)

        titleText.text = deck.title
        countText.text = "${deck.cardCount} Cards"

        return view
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// FILE 5 of 5 │ DashboardActivity.kt
// ─────────────────────────────────────────────────────────────────────────────

// package com.example.reviewbuddy.screens.dashboard   ← same package

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
