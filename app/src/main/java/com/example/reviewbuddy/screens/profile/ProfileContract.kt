package com.example.reviewbuddy.screens.profile

interface ProfileContract {
    interface View {
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onBackToDashboardClicked()
    }
}
