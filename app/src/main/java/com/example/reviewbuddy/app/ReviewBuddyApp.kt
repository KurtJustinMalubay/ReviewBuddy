package com.example.reviewbuddy.app

import android.app.Application
import android.content.Context
import com.example.reviewbuddy.data.repositories.DeckRepository

class ReviewBuddyApp : Application() {
    
    companion object {
        lateinit var appContext: Context
            private set
        lateinit var deckRepository: DeckRepository
            private set
        lateinit var userRepository: com.example.reviewbuddy.data.repositories.UserRepository
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        deckRepository = DeckRepository()
        userRepository = com.example.reviewbuddy.data.repositories.UserRepository()
    }
}
