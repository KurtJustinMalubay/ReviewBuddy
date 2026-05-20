package com.example.reviewbuddy.data.models

data class Deck(
    val id: String,
    var title: String,
    val cards: MutableList<Card> = mutableListOf(),
    var isPinned: Boolean = false,
    var lastAccessed: Long = 0L,
    var folder: String? = null
) {
    val cardCount: Int
        get() = cards.size
}
