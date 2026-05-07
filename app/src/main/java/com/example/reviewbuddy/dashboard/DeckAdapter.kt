package com.example.reviewbuddy.dashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Deck

class DeckAdapter(context: Context, private val decks: List<Deck>) : ArrayAdapter<Deck>(context, 0, decks) {

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
