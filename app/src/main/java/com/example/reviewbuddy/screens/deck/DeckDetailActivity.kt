package com.example.reviewbuddy.screens.deck

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Card

class DeckDetailActivity : Activity(), DeckDetailContract.View {

    private lateinit var presenter: DeckDetailContract.Presenter
    private lateinit var listViewCards: ListView
    private lateinit var textviewEmptyState: TextView
    private lateinit var cardAdapter: CardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deck_detail_layout)

        presenter = DeckDetailPresenter(this)

        listViewCards = findViewById(R.id.listViewCards)
        textviewEmptyState = findViewById(R.id.textviewEmptyState)
        
        val buttonBack = findViewById<android.view.View>(R.id.buttonBack)
        val buttonAddCard = findViewById<android.view.View>(R.id.buttonAddCard)
        val buttonStudy = findViewById<Button>(R.id.buttonStudy)

        val deckId = intent.getStringExtra("DECK_ID")
        presenter.loadDeck(deckId)

        buttonBack.setOnClickListener {
            presenter.onBackClicked()
        }

        buttonAddCard.setOnClickListener {
            presenter.onAddCardClicked()
        }

        buttonStudy.setOnClickListener {
            presenter.onStudyClicked()
        }

        listViewCards.setOnItemLongClickListener { _, _, position, _ ->
            val card = cardAdapter.getItem(position)
            if (card != null) {
                presenter.onCardLongClicked(card)
            }
            true
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

    override fun showCards(cards: List<Card>) {
        cardAdapter = CardAdapter(this, cards)
        listViewCards.adapter = cardAdapter
    }

    override fun toggleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            listViewCards.visibility = View.GONE
            textviewEmptyState.visibility = View.VISIBLE
        } else {
            listViewCards.visibility = View.VISIBLE
            textviewEmptyState.visibility = View.GONE
        }
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showAddCardDialog() {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_add_card)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val edittextFront = dialog.findViewById<EditText>(R.id.edittextCardFront)
        val edittextBack = dialog.findViewById<EditText>(R.id.edittextCardBack)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancelCard)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveCard)

        buttonCancel.setOnClickListener { dialog.dismiss() }

        buttonSave.setOnClickListener {
            presenter.confirmAddCard(
                edittextFront.text.toString(),
                edittextBack.text.toString()
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun showEditCardDialog(card: Card) {
        val dialog = android.app.Dialog(this)
        dialog.setContentView(R.layout.dialog_add_card)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setLayout(
            android.view.ViewGroup.LayoutParams.MATCH_PARENT,
            android.view.ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val title = dialog.findViewById<TextView>(R.id.textviewCardDialogTitle)
        title.text = "Edit Flashcard"

        val edittextFront = dialog.findViewById<EditText>(R.id.edittextCardFront)
        val edittextBack = dialog.findViewById<EditText>(R.id.edittextCardBack)
        val buttonCancel = dialog.findViewById<Button>(R.id.buttonCancelCard)
        val buttonSave = dialog.findViewById<Button>(R.id.buttonSaveCard)

        edittextFront.setText(card.front)
        edittextBack.setText(card.back)

        buttonCancel.setOnClickListener { dialog.dismiss() }

        // Change 'Cancel' to 'Delete' for long press maybe? No, let's keep it simple.
        // Or let's add a long click to delete, or just add a delete button programmatically? 
        // For now, let's change the cancel button text to 'Delete' just to allow deletion.
        buttonCancel.text = "Delete"
        buttonCancel.setTextColor(resources.getColor(R.color.danger, theme))
        
        buttonCancel.setOnClickListener {
            presenter.confirmDeleteCard(card.id)
            dialog.dismiss()
        }

        buttonSave.setOnClickListener {
            presenter.confirmEditCard(
                card.id,
                edittextFront.text.toString(),
                edittextBack.text.toString()
            )
            dialog.dismiss()
        }

        dialog.show()
    }

    override fun launchStudyMode(deckId: String) {
        val intent = android.content.Intent(this, com.example.reviewbuddy.screens.study.StudyActivity::class.java)
        intent.putExtra("DECK_ID", deckId)
        startActivity(intent)
    }
}
