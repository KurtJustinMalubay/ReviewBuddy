package com.example.reviewbuddy.screens.decks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck

class DecksListAdapter(
    private var decks: List<Deck>,
    private val onDeckClick: (Deck) -> Unit,
    private val onDeckLongClick: (Deck) -> Unit
) : RecyclerView.Adapter<DecksListAdapter.DeckListViewHolder>() {

    class DeckListViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.textTitle)
        val countText: TextView = view.findViewById(R.id.textCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeckListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_deck_list, parent, false)
        return DeckListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeckListViewHolder, position: Int) {
        val deck = decks[position]
        holder.titleText.text = deck.title
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
