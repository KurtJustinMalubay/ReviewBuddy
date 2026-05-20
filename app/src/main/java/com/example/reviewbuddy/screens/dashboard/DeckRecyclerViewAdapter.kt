package com.example.reviewbuddy.screens.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck

class DeckRecyclerViewAdapter(
    private var decks: List<Deck>,
    private val onDeckClick: (Deck) -> Unit,
    private val onDeckLongClick: (Deck) -> Unit
) : RecyclerView.Adapter<DeckRecyclerViewAdapter.DeckViewHolder>() {

    class DeckViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.textTitle)
        val countText: TextView = view.findViewById(R.id.textCount)
        val iconImage: ImageView = view.findViewById(R.id.imageDeckIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck, parent, false)
        return DeckViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckViewHolder, position: Int) {
        val deck = decks[position]
        holder.titleText.text = if (deck.isPinned) "📌 ${deck.title}" else deck.title
        holder.countText.text = "${deck.cardCount} Cards"
        
        holder.itemView.setOnClickListener { onDeckClick(deck) }
        holder.itemView.setOnLongClickListener {
            onDeckLongClick(deck)
            true
        }
    }

    override fun getItemCount(): Int = decks.size

    fun updateDecks(newDecks: List<Deck>) {
        this.decks = newDecks
        notifyDataSetChanged()
    }
}
