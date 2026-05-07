package com.example.reviewbuddy.screens.profile

interface ProfileContract {
    interface View {
        fun showProfileDetails(
            firstName: String,
            middleName: String,
            lastName: String,
            username: String,
            email: String,
            course: String,
            initial: String
        )
        fun navigateToDashboard()
    }

    interface Presenter {
        fun loadProfile()
        fun onBackToDashboardClicked()
        fun onBackClicked()
    }
}
