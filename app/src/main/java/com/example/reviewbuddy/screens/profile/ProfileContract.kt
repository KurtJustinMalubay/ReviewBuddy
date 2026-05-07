package com.example.reviewbuddy.screens.profile

interface ProfileContract {
    interface View {
        fun showProfileName(name: String)
        fun navigateToDashboard()
    }

    interface Presenter {
        fun loadProfile()
        fun onBackToDashboardClicked()
        fun onBackClicked()
    }
}
