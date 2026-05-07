package com.example.reviewbuddy.profile

interface ProfileContract {
    interface View {
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onBackToDashboardClicked()
    }
}
