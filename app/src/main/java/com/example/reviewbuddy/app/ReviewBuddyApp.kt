package com.example.reviewbuddy.app

import android.app.Application
import com.example.reviewbuddy.data.repositories.DeckRepository

class ReviewBuddyApp : Application() {
    
    companion object {
        lateinit var deckRepository: DeckRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        deckRepository = DeckRepository()
    }
}
