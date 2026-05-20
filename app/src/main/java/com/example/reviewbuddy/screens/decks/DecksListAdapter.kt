package com.example.reviewbuddy.screens.decks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck

/**
 * RecyclerView Adapter responsible for rendering study Decks.
 * Supports standard click execution, single item long-press options, and bulk multi-selection modes.
 */
class DecksListAdapter(
    private var decks: List<Deck>,
    private val onDeckClick: (Deck) -> Unit,
    private val onDeckLongClick: (Deck) -> Unit
) : RecyclerView.Adapter<DecksListAdapter.DeckListViewHolder>() {

    /**
     * Manages selection state. Automatically clears selection collection
     * and triggers size updates to clear overlays when turned off.
     */
    var isSelectionMode = false
        set(value) {
            field = value
            if (!value) {
                selectedDeckIds.clear()
                onSelectionChanged?.invoke(0)
            }
            notifyDataSetChanged()
        }

    val selectedDeckIds = mutableSetOf<String>()
    var onSelectionChanged: ((Int) -> Unit)? = null

    /**
     * ViewHolder for caching individual deck row widgets.
     */
    class DeckListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardDeckView: CardView = view.findViewById(R.id.cardDeckView)
        val titleText: TextView = view.findViewById(R.id.textTitle)
        val countText: TextView = view.findViewById(R.id.textCount)
        val imageIndicator: ImageView = view.findViewById(R.id.imageIndicator)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck_list, parent, false)
        return DeckListViewHolder(view)
    }

    /**
     * Binds deck properties, pinned indicator badges, folder filters,
     * dynamic selection indicators, and click listener actions.
     */
    override fun onBindViewHolder(holder: DeckListViewHolder, position: Int) {
        val deck = decks[position]
        holder.titleText.text = if (deck.isPinned) "📌 ${deck.title}" else deck.title
        
        // Show folder name if assigned to folder!
        if (!deck.folder.isNullOrBlank()) {
            holder.countText.text = "${deck.cardCount} Cards • Folder: ${deck.folder}"
        } else {
            holder.countText.text = "${deck.cardCount} Cards"
        }

        // Apply visual highlights based on active multi-selection state
        val isSelected = selectedDeckIds.contains(deck.id)
        if (isSelectionMode) {
            holder.cardDeckView.setCardBackgroundColor(
                if (isSelected) android.graphics.Color.parseColor("#EEF2FF")
                else android.graphics.Color.WHITE
            )
            holder.imageIndicator.setImageResource(R.drawable.ic_check)
            holder.imageIndicator.setImageTintList(
                android.content.res.ColorStateList.valueOf(
                    if (isSelected) android.graphics.Color.parseColor("#4F46E5")
                    else android.graphics.Color.parseColor("#CBD5E1")
                )
            )
        } else {
            holder.cardDeckView.setCardBackgroundColor(android.graphics.Color.WHITE)
            holder.imageIndicator.setImageResource(R.drawable.ic_chevron_right)
            holder.imageIndicator.setImageTintList(
                android.content.res.ColorStateList.valueOf(android.graphics.Color.parseColor("#E2E8F0"))
            )
        }

        holder.itemView.setOnClickListener {
            if (isSelectionMode) {
                toggleSelection(deck.id)
            } else {
                onDeckClick(deck)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (!isSelectionMode) {
                onDeckLongClick(deck)
            }
            true
        }
    }

    override fun getItemCount(): Int = decks.size

    /**
     * Updates the datasets adapter contents and refreshes list animations.
     */
    fun updateDecks(newDecks: List<Deck>) {
        this.decks = newDecks
        notifyDataSetChanged()
    }

    /**
     * Toggles the selection checkmark status of a clicked deck item.
     */
    private fun toggleSelection(deckId: String) {
        if (selectedDeckIds.contains(deckId)) {
            selectedDeckIds.remove(deckId)
        } else {
            selectedDeckIds.add(deckId)
        }
        
        if (isSelectionMode && selectedDeckIds.isEmpty()) {
            isSelectionMode = false
        } else {
            notifyDataSetChanged()
            onSelectionChanged?.invoke(selectedDeckIds.size)
        }
    }

    /**
     * Explicit helper to terminate selection mode.
     */
    fun clearSelection() {
        isSelectionMode = false
    }
}
