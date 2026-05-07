package com.example.reviewbuddy.screens.profile

interface ProfileContract {
    interface View {
        fun showProfileDetails(
            firstName: String,
            lastName: String,
            course: String,
            deckCount: Int,
            cardCount: Int
        )
        fun navigateToDashboard()
        fun navigateToDecks()
        fun navigateToLogin()
        fun showComingSoonMessage(feature: String)
    }

    interface Presenter {
        fun loadProfile()
        fun onBackToDashboardClicked()
        fun onLogoutClicked()
        fun onSettingsClicked()
        fun onNotificationsClicked()
        fun onHomeClicked()
        fun onDecksClicked()
    }
}
