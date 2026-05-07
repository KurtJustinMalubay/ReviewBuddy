package com.example.reviewbuddy.data.models

data class Deck(
    val id: String,
    var title: String,
    val cards: MutableList<Card> = mutableListOf()
) {
    val cardCount: Int
        get() = cards.size
}
