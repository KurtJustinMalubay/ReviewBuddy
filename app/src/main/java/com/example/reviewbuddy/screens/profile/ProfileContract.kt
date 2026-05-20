package com.example.reviewbuddy.screens.profile

interface ProfileContract {
    interface View {
        fun showProfileDetails(
            username: String,
            email: String,
            fullName: String,
            firstName: String,
            middleName: String,
            lastName: String,
            course: String
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
