package com.example.reviewbuddy.screens.deck

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.reviewbuddy.R
import com.example.reviewbuddy.data.models.Card

class CardAdapter(context: Context, cards: List<Card>) : ArrayAdapter<Card>(context, 0, cards) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false)
        }

        val card = getItem(position)
        val textFront = view!!.findViewById<TextView>(R.id.textFront)
        val textBack = view.findViewById<TextView>(R.id.textBack)

        if (card != null) {
            textFront.text = card.front
            textBack.text = card.back
        }

        return view
    }
}
